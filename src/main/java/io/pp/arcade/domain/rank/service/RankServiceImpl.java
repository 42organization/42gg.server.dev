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
import org.apache.tomcat.jni.Local;
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
    private final RedisTemplate redisTemplate;
    private final SeasonRepository seasonRepository;

    @Transactional
    public RankFindListDto findRankList(Pageable pageable, Integer count, GameType type) {
        int currentPage = pageable.getPageNumber();
        int totalPage = redisTemplate.opsForZSet().size(type.getKey()).intValue() / count;
        int start = currentPage * count;
        int end = start + count;
        Set<String> reverseRange = redisTemplate.opsForZSet().reverseRange(type.getKey(), start, end);
        List<RankUserDto> rankList = getUserRankList(reverseRange, type);
        RankFindListDto findListDto = RankFindListDto.builder()
                .currentPage(currentPage > totalPage ? totalPage : currentPage) // 최대값은 totalPage
                .totalPage(totalPage)
                .rankList(rankList)
                .build();
        return findListDto;
    }

    @Transactional
    public RankUserDto findRankById(RankFindDto findDto) {
        String userKey = getUserKey(findDto.getIntraId(), findDto.getGameType());
        String rankKey = getRankKey(findDto.getIntraId(), findDto.getGameType());
        RankRedis userRankInfo = (RankRedis) redisTemplate.opsForValue().get(userKey);
        Long userRanking = redisTemplate.opsForZSet().reverseRank(findDto.getGameType().getKey(), rankKey);

        if (userRanking == null) {
            throw new BusinessException("{server.internal.error}");
        }

        RankUserDto infoDto = RankUserDto.builder()
                .intraId(userRankInfo.getIntraId())
                .ppp(userRankInfo.getPpp())
                .wins(userRankInfo.getWins()).losses(userRankInfo.getLosses())
                .winRate(userRankInfo.getWinRate())
                .rank(userRanking.intValue())
                .statusMessage(userRankInfo.getStatusMessage())
                .build();
        return infoDto;
    }

    @Transactional
    public void modifyUserPpp(RankModifyDto modifyDto) {
        String userKey = getUserKey(modifyDto.getIntraId(), modifyDto.getGameType());
        String rankKey = getRankKey(modifyDto.getIntraId(), modifyDto.getGameType());
        RankRedis rank = (RankRedis) redisTemplate.opsForValue().get(userKey);
        rank.update(modifyDto.getIsWin(), modifyDto.getPpp());
        redisTemplate.opsForValue().set(userKey, rank);
        redisTemplate.opsForZSet().add(modifyDto.getGameType().getKey(), rankKey, modifyDto.getPpp());
    }

    @Transactional
    public void modifyRankStatusMessage(RankModifyStatusMessageDto modifyDto) {
        String userKey = getUserKey(modifyDto.getIntraId(), modifyDto.getGameType());
        RankRedis rank = (RankRedis) redisTemplate.opsForValue().get(userKey);
        rank.setStatusMessage(modifyDto.getStatusMessage());
        redisTemplate.opsForValue().set(userKey, rank);
    }

    @Transactional
    public void saveAllToRDB(RankSaveAllDto saveAllDto) {
        LocalDateTime now = LocalDateTime.now();
        Season season = seasonRepository.findSeasonByStartTimeIsBeforeAndEndTimeIsAfter(now, now).orElseThrow(() -> new BusinessException("{server.internal.error}"));
        List<Rank> ranks = rankRepository.findAllBySeasonId(season.getId());
        if (ranks.isEmpty()) {
            ListOperations listOperations = redisTemplate.opsForList();

        }


        // 랭크 테이블에 해당 시즌의 정보가 있을 경우 유저정보 업데이트

        // 랭크 테이블에 해당 시즌의 정보가 없을 경우 새로운 유저 생성

        rankRepository.saveAll(ranks);
    }

    @Transactional
    public Boolean isEmpty() {
        return !redisTemplate.hasKey(Key.SINGLE);
    }

    @Transactional
    public void loadAllFromRDB() {
        LocalDateTime now = LocalDateTime.now();
        Season season = seasonRepository.findSeasonByStartTimeIsBeforeAndEndTimeIsAfter(now, now).orElseThrow(() -> new BusinessException("{server.internal.error}"));
        List<Rank> ranks = rankRepository.findAllBySeasonId(season.getId());
        ranks.forEach(rank -> {
            rankToRedisRank(rank);
        });
    }

    @Transactional
    public void userToRedisRank(User user) {
        String intraId = user.getIntraId();
        if (redisTemplate.opsForValue().get(intraId + Key.SINGLE) == null) {
            RankRedis singleRank = RankRedis.from(user, Key.SINGLE);
            RankRedis doubleRank = RankRedis.from(user, Key.DOUBLE);
            redisTemplate.opsForValue().set(getUserKey(intraId, GameType.valueOf(Key.SINGLE)), singleRank);
            redisTemplate.opsForValue().set(getUserKey(intraId, GameType.valueOf(Key.DOUBLE)), doubleRank);
        }
        redisTemplate.opsForZSet().add(Key.SINGLE, getRankKey(intraId, GameType.valueOf(Key.SINGLE)), user.getPpp());
        redisTemplate.opsForZSet().add(Key.DOUBLE, getRankKey(intraId, GameType.valueOf(Key.DOUBLE)), user.getPpp());
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
        List<RankUserDto> rankList = new ArrayList<>();
        range.forEach(intraId -> {
            String userKey = getUserKey(intraId, type);
            RankRedis userInfo = (RankRedis) redisTemplate.opsForValue().get(userKey);
            Integer totalGames = userInfo.getLosses() + userInfo.getWins();
            Integer rank = (totalGames == 0) ? -1 : redisTemplate.opsForZSet().reverseRank(type.getKey(), getRankKey(intraId, type)).intValue();
            rankList.add(RankUserDto.builder()
                    .intraId(userInfo.getIntraId())
                    .ppp(userInfo.getPpp())
                    .rank(rank + 1)
                    .statusMessage(userInfo.getStatusMessage())
                    .losses(userInfo.getLosses())
                    .wins(userInfo.getWins())
                    .winRate(userInfo.getWinRate())
                    .build());
        });
        return rankList;
    }

    private String getUserKey(String intraId, GameType gameType) {
        return Key.RANK_USER + intraId + gameType.getKey();
    }

    private String getRankKey(String intraId, GameType gameType) {
        return intraId + gameType.getKey();
    }

    private void rankToRedisRank(Rank rank) {
        User user = rank.getUser();
        userToRedisRank(user);
    }
}
