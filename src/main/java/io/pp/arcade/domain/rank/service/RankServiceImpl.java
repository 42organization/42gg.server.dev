package io.pp.arcade.domain.rank.service;

import io.pp.arcade.domain.admin.dto.create.RankCreateRequestDto;
import io.pp.arcade.domain.admin.dto.delete.RankDeleteDto;
import io.pp.arcade.domain.admin.dto.update.RankUpdateRequestDto;
import io.pp.arcade.domain.rank.Rank;
import io.pp.arcade.domain.season.Season;
import io.pp.arcade.domain.season.SeasonRepository;
import io.pp.arcade.global.redis.Key;
import io.pp.arcade.domain.rank.RankRedis;
import io.pp.arcade.domain.rank.RankRepository;
import io.pp.arcade.domain.rank.dto.*;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.exception.BusinessException;
import io.pp.arcade.global.type.GameType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class RankServiceImpl implements RankNTService {
    private final UserRepository userRepository;
    private final RankRepository rankRepository;
    private final RedisTemplate<String, String> redisRank;
    private final RedisTemplate<String, RankRedis> redisUserInfo;
    private final SeasonRepository seasonRepository;

    @Transactional
    public RankFindListDto findRankList(Pageable pageable, Integer count, GameType type) {
        int currentPage = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int totalPage = (redisRank.opsForZSet().size(getRankKey(type)).intValue() / count) + 1;
        int start = currentPage * count;
        int end = start + count;

        Set<String> reverseRange = redisRank.opsForZSet().reverseRange(getRankKey(type), start, end);

        List<RankUserDto> rankList = getUserRankList(reverseRange, type);
        RankFindListDto findListDto = RankFindListDto.builder()
                .currentPage(pageable.getPageNumber() > totalPage ? totalPage : pageable.getPageNumber() ) // 최대값은 totalPage
                .totalPage(totalPage)
                .rankList(rankList)
                .build();
        return findListDto;
    }

    @Transactional
    public RankUserDto findRankById(RankFindDto findDto) {
        String userKey = getUserKey(findDto.getIntraId(), findDto.getGameType());
        String userRankKey = "\"" + getUserRankKey(findDto.getIntraId(), findDto.getGameType()) + "\"";
        RankRedis userRankInfo = redisUserInfo.opsForValue().get(userKey);
        Long userRanking = redisRank.opsForZSet().reverseRank(getRankKey(findDto.getGameType()), userRankKey);

        if (userRanking == null) {
            throw new BusinessException("{server.internal.error}");
        }

        RankUserDto infoDto = RankUserDto.builder()
                .intraId(userRankInfo.getIntraId())
                .ppp(userRankInfo.getPpp())
                .wins(userRankInfo.getWins()).losses(userRankInfo.getLosses())
                .winRate(userRankInfo.getWinRate())
                .rank(getRanking(userRankInfo, findDto.getGameType()))
                .statusMessage(userRankInfo.getStatusMessage())
                .build();
        return infoDto;
    }

    @Transactional
    public void modifyUserPpp(RankModifyDto modifyDto) {
        String userKey = getUserKey(modifyDto.getIntraId(), modifyDto.getGameType());
        String rankKey = getUserRankKey(modifyDto.getIntraId(), modifyDto.getGameType());
        RankRedis rank = redisUserInfo.opsForValue().get(userKey);
        rank.update(modifyDto.getIsWin(), modifyDto.getPpp());
        redisUserInfo.opsForValue().set(userKey, rank);
        redisRank.opsForZSet().add(modifyDto.getGameType().getCode(), rankKey, modifyDto.getPpp());
    }

    @Transactional
    public void modifyRankStatusMessage(RankModifyStatusMessageDto modifyDto) {
        String userKey = getUserKey(modifyDto.getIntraId(), modifyDto.getGameType());
        RankRedis rank = redisUserInfo.opsForValue().get(userKey);
        rank.setStatusMessage(modifyDto.getStatusMessage());
        redisUserInfo.opsForValue().set(userKey, rank);
    }

    @Transactional
    public void saveAllToRDB(RankSaveAllDto saveAllDto) {
        LocalDateTime now = LocalDateTime.now();
        Season season = seasonRepository.findSeasonByStartTimeIsBeforeAndEndTimeIsAfter(now, now).orElseThrow(() -> new BusinessException("{server.internal.error}"));
        List<Rank> ranks = rankRepository.findAllBySeasonId(season.getId());
        if (ranks.isEmpty()) {
            ListOperations listOperations = redisRank.opsForList();

        }


        // 랭크 테이블에 해당 시즌의 정보가 있을 경우 유저정보 업데이트

        // 랭크 테이블에 해당 시즌의 정보가 없을 경우 새로운 유저 생성

        rankRepository.saveAll(ranks);
    }

    @Transactional
    public Boolean isEmpty() {
        return !redisRank.hasKey(Key.SINGLE);
    }

    @Transactional
    public void loadAllFromRDB() {
        List<Rank> ranks = rankRepository.findAll();
        ranks.forEach(rank -> {
            rankToRedisRank(rank);
        });
        /*
        LocalDateTime now = LocalDateTime.now();
        Season season = seasonRepository.findSeasonByStartTimeIsBeforeAndEndTimeIsAfter(now, now).orElse(null);
        if (season != null) {
            List<Rank> ranks = rankRepository.findAllBySeasonId(season.getId());
            ranks.forEach(rank -> {
                rankToRedisRank(rank);
            });
        }
        */
    }

    @Transactional
    public void userToRedisRank(User user) {
        String intraId = user.getIntraId();
        if (redisUserInfo.opsForValue().get(getUserKey(intraId, GameType.valueOf(Key.SINGLE))) == null) {
            RankRedis singleRank = RankRedis.from(user, Key.SINGLE);
            RankRedis doubleRank = RankRedis.from(user, Key.BUNGLE);
            redisUserInfo.opsForValue().set(getUserKey(intraId, GameType.valueOf(Key.SINGLE)), singleRank);
            redisUserInfo.opsForValue().set(getUserKey(intraId, GameType.valueOf(Key.BUNGLE)), doubleRank);
        }
        redisRank.opsForZSet().add(Key.SINGLE, getUserRankKey(intraId, GameType.valueOf(Key.SINGLE)), user.getPpp());
        redisRank.opsForZSet().add(Key.BUNGLE, getUserRankKey(intraId, GameType.valueOf(Key.BUNGLE)), user.getPpp());
    }


    /* Admin method */

    @Transactional
    public void createRankByAdmin(RankCreateRequestDto createRequestDto) {
        User user = userRepository.findById(createRequestDto.getUserId()).orElseThrow();
        Rank rank = Rank.builder()
                .user(user)
                .seasonId(createRequestDto.getSeasonId())
                .racketType(createRequestDto.getRacketType())
                .ppp(createRequestDto.getPpp())
                .ranking(createRequestDto.getRangking())
                .wins(createRequestDto.getWins())
                .losses(createRequestDto.getLosses())
                .build();
        rankRepository.save(rank);
    }

    @Transactional
    public void updateRankByAdmin(RankUpdateRequestDto updateRequestDto) {
        Rank rank = rankRepository.findById(updateRequestDto.getRankId()).orElseThrow();
        rank.setPpp(updateRequestDto.getPpp());
    }

    @Transactional
    public void deleteRankByAdmin(RankDeleteDto deleteDto) {
        Rank rank = rankRepository.findById(deleteDto.getRankId()).orElseThrow();
        rankRepository.delete(rank);
    }

    @Transactional
    public List<RankDto> findRankByAdmin(Pageable pageable) {
        Page<Rank> ranks = rankRepository.findAll(pageable);
        List<RankDto> rankDtos = ranks.stream().map(RankDto::from).collect(Collectors.toList());
        return rankDtos;
    }

    @Transactional
    public List<RankDto> findAll() {
        List<Rank> ranks = rankRepository.findAll();
        List<RankDto> rankDtos = ranks.stream().map(RankDto::from).collect(Collectors.toList());
        return rankDtos;
    }


    /* Private Method */

    private List<RankUserDto> getUserRankList(Set<String> range, GameType type) {
        List<RankUserDto> rankList = new ArrayList<RankUserDto>();
        range.forEach(key -> {
            String userKey = getUserKey(key.replaceAll("\"",""));
            RankRedis userRankInfo = redisUserInfo.opsForValue().get(userKey);
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
        return intraId + gameType.getCode();
    }

    private String getRankKey(GameType gameType) {
        return gameType.getCode();
    }

    private Integer getRanking(RankRedis userInfo ,GameType gameType){
        Integer totalGames = userInfo.getLosses() + userInfo.getWins();
        Integer ranking= (totalGames == 0) ? -1 : redisRank.opsForZSet().reverseRank(getRankKey(gameType), getUserRankKey(userInfo.getIntraId(), gameType)).intValue() + 1;
        return ranking;
    }
    private void rankToRedisRank(Rank rank) {
        User user = rank.getUser();
        userToRedisRank(user);
    }
}
