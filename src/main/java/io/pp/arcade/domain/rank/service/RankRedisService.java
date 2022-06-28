package io.pp.arcade.domain.rank.service;

import io.jsonwebtoken.lang.Collections;
import io.pp.arcade.domain.admin.dto.create.RankCreateRequestDto;
import io.pp.arcade.domain.admin.dto.delete.RankDeleteDto;
import io.pp.arcade.domain.admin.dto.update.RankUpdateRequestDto;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.redis.Key;
import io.pp.arcade.domain.rank.RankRedis;
import io.pp.arcade.domain.rank.dto.*;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.type.GameType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class RankRedisService implements RankNTService {
    private final RedisTemplate<String, String> redisRank;
    private final RedisTemplate<String, RankRedis> redisUser;

    @Transactional
    public RankListDto findRankList(RankFindListDto rankFindListDto) {
        int pageNumber = rankFindListDto.getPageable().getPageNumber();
        int count =rankFindListDto.getCount();
        GameType type = rankFindListDto.getGameType();

        int currentPage = (pageNumber < 1) ? 1 : (pageNumber);
        Long size = redisRank.opsForZSet().size(getRankKey(type));
        int totalPage = (size == null) ? 1 : size.intValue() / count + 1;
        int start = (currentPage - 1) * count;
        int end = start + count - 1;

        Set<String> reverseRange = redisRank.opsForZSet().reverseRange(getRankKey(type), start, end);
        List<RankUserDto> rankUserList = getUserRankList(reverseRange, type);
        RankListDto findListDto = RankListDto.builder()
                .currentPage(currentPage > totalPage ? totalPage : currentPage) // 최대값은 totalPage
                .totalPage(totalPage)
                .rankList(rankUserList)
                .build();
        return findListDto;
    }

    @Transactional
    public RankUserDto findRankById(RankFindDto findDto) {
        String userKey = getUserKey(findDto.getIntraId(), findDto.getGameType());
        String userRankKey = getUserRankKey(findDto.getIntraId(), findDto.getGameType());
        RankRedis userRankInfo = redisUser.opsForValue().get(userKey);
        Long userRanking = redisRank.opsForZSet().reverseRank(getRankKey(findDto.getGameType()), userRankKey);
        return (userRankInfo == null) ? null : RankUserDto.from(userRankInfo, userRanking);
    }

    @Transactional
    public void modifyUserPpp(RankModifyDto modifyDto) {
        String userKey = getUserKey(modifyDto.getIntraId(), modifyDto.getGameType());
        String rankKey = getUserRankKey(modifyDto.getIntraId(), modifyDto.getGameType());
        RankRedis rank = redisUser.opsForValue().get(userKey);
        rank.update(modifyDto.getIsWin(), modifyDto.getPpp());
        redisUser.opsForValue().set(userKey, rank);
        redisRank.opsForZSet().add(modifyDto.getGameType().getCode(), rankKey, modifyDto.getPpp());
    }

    @Transactional
    public void modifyRankStatusMessage(RankModifyStatusMessageDto modifyDto) {
        String singleUserKey = getUserKey(modifyDto.getIntraId(), GameType.SINGLE);
        String bungleUserKey = getUserKey(modifyDto.getIntraId(), GameType.BUNGLE);
        RankRedis singleRank = redisUser.opsForValue().get(singleUserKey);
        RankRedis bungleRank = redisUser.opsForValue().get(bungleUserKey);
        singleRank.setStatusMessage(modifyDto.getStatusMessage());
        bungleRank.setStatusMessage(modifyDto.getStatusMessage());
        redisUser.opsForValue().set(singleUserKey, singleRank);
        redisUser.opsForValue().set(bungleUserKey, bungleRank);
    }

    @Transactional
    public List<RankRedisDto> findRankAll(){
        Set rankKeys = redisRank.keys(Key.RANK_USER_ALL);
        List<RankRedis> rankList = redisUser.opsForValue().multiGet(rankKeys);
        List<RankRedisDto> rankRedisDtos = new ArrayList<RankRedisDto>();
        if (!Collections.isEmpty(rankList)) {
            rankRedisDtos = rankList.stream().map(rank -> {
                String key = getUserKey(rank.getIntraId(), rank.getGameType());
                Integer singleRanking = getRanking(rank, GameType.SINGLE);
                return RankRedisDto.from(rank, singleRanking.intValue());
            }).collect(Collectors.toList());
        }
        return rankRedisDtos;
    }

    @Transactional
    public Boolean isEmpty() {
        return !redisRank.hasKey(Key.SINGLE);
    }


    @Transactional
    public void saveAll(List<RankDto> rankDtos) {
        rankDtos.forEach(rankDto -> {
            toRedisRank(rankDto);
        });
    }
    /* Admin method */

    @Transactional
    public void createRankByAdmin(RankCreateRequestDto createRequestDto) {

    }

    @Transactional
    public void updateRankByAdmin(RankUpdateRequestDto updateRequestDto) {

    }

    @Transactional
    public void deleteRankByAdmin(RankDeleteDto deleteDto) {

    }

    @Transactional
    public List<RankDto> findRankByAdmin(Pageable pageable) {
       return null;
    }


    /* Private Method */
    private List<RankUserDto> getUserRankList(Set<String> range, GameType type) {
        List<RankUserDto> rankList = new ArrayList<RankUserDto>();
        range.forEach(key -> {
            String userKey = getUserKey(key.replaceAll("\"",""));
            RankRedis userRankInfo = redisUser.opsForValue().get(userKey);
            rankList.add(RankUserDto.builder()
                    .intraId(userRankInfo.getIntraId())
                    .ppp(userRankInfo.getPpp())
                    .rank(getRanking(userRankInfo, type))
                    .statusMessage(userRankInfo.getStatusMessage())
                    .losses(userRankInfo.getLosses())
                    .wins(userRankInfo.getWins())
                    .winRate(userRankInfo.getWinRate())
                    .build());
        });
        return rankList;
    }

    private String getUserKey(String key) { return Key.RANK_USER + key; }

    private String getUserKey(String intraId, GameType gameType) {
        return Key.RANK_USER + intraId + gameType.getCode();
    }

    private String getUserRankKey(String intraId, GameType gameType) {
        return "\"" + intraId + gameType.getCode() + "\"";
    }

    private String getRankKey(GameType gameType) {
        return gameType.getCode();
    }

    private Integer getRanking(RankRedis userInfo ,GameType gameType){
        Integer totalGames = userInfo.getLosses() + userInfo.getWins();
        Integer ranking= (totalGames == 0) ? -1 : redisRank.opsForZSet().reverseRank(getRankKey(gameType), getUserRankKey(userInfo.getIntraId(), gameType)).intValue() + 1;
        return ranking;
    }

    private void toRedisRank(RankDto rankDto) {
        UserDto user = rankDto.getUser();
        userToRedisRank(user);
    }

    @Transactional
    public void userToRedisRank(UserDto user) {
        String intraId = user.getIntraId();
        if (redisUser.opsForValue().get(getUserKey(intraId, GameType.valueOf(Key.SINGLE))) == null) {
            RankRedis singleRank = RankRedis.from(user, Key.SINGLE);
            RankRedis doubleRank = RankRedis.from(user, Key.BUNGLE);
            redisUser.opsForValue().set(getUserKey(intraId, GameType.valueOf(Key.SINGLE)), singleRank);
            redisUser.opsForValue().set(getUserKey(intraId, GameType.valueOf(Key.BUNGLE)), doubleRank);
        }
        redisRank.opsForZSet().add(Key.SINGLE, getUserRankKey(intraId, GameType.valueOf(Key.SINGLE)), user.getPpp());
        redisRank.opsForZSet().add(Key.BUNGLE, getUserRankKey(intraId, GameType.valueOf(Key.BUNGLE)), user.getPpp());
    }
}
