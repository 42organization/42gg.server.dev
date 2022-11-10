package io.pp.arcade.v1.domain.rank;

import io.pp.arcade.v1.domain.rank.dto.*;
import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import io.pp.arcade.v1.domain.rank.type.RankType;
import io.pp.arcade.v1.global.type.GameType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RankRedisRepository {
    private final RedisTemplate<String, String> redis;
    private final ListOperations<String, RankRedis> redisRankList;
    private final RedisKeyManager redisKeyManager;

    public RankRedis findRank(String key, Integer index) {
        return redisRankList.index(key, index - 1);
    }

    public List<RankRedis> findAllRank(String key) {
        return redisRankList.range(key, 0, -1);
    }
    public Integer findRanking(RedisRankingFindDto findDto){
        RankRedis rank = findDto.getRank();
        GameType gameType = rank.getGameType();

        Integer ranking = RankType.UN_RANK;
        Integer totalGames = rank.getLosses() + rank.getWins();
        if (totalGames > 0) {
            String userIndex = rank.getId().toString();
            String rankingKey = redisKeyManager.getRankingKeyBySeason(findDto.getKeyGetDto(), gameType);
            ranking = redis.opsForZSet().reverseRank(rankingKey, userIndex).intValue() + 1; /* +1은 0부터 시작하기에 */
        }
        return ranking;
    }

    public void addRank(RedisRankAddDto addDto) {
        String rankingKey = addDto.getKey();
        RankRedis rank = addDto.getRank();
        redisRankList.rightPush(rankingKey, rank);
    }

    public void addRanking(RedisRankingAddDto addDto) {
        RankRedis rank = addDto.getRank();
        String rankingKey = redisKeyManager.getCurrentRankingKey(rank.getGameType());
        String userIndex = rank.getId().toString();
        Integer isPlayer =  addDto.getRank().getLosses() + addDto.getRank().getWins() > 0 ? RankType.RANK_PLAYER : RankType.UN_RANKPLAYER;
        redis.opsForZSet().add(rankingKey, userIndex, rank.getPpp() * isPlayer);
    }

    public void updateRank(RedisRankUpdateDto updateDto) {
        redisRankList.set(updateDto.getSeasonKey(), updateDto.getUserId() - 1 , updateDto.getUserRank());
    }

    public void updateRanking(RedisRankingUpdateDto updateDto ) {
        String userIndex = updateDto.getRank().getId().toString();
        redis.opsForZSet().add(updateDto.getRankingKey(), userIndex, updateDto.getPpp());
    }

    public List<RankUserDto> findRankingList(RedisRankingFindListDto findListDto) {
        String curRankingKey = findListDto.getCurRankingKey();
        Integer start = findListDto.getStart();
        Integer end = findListDto.getEnd();
        Set<String> range = redis.opsForZSet().reverseRange(curRankingKey, start, end);

        List<RankUserDto> rankList = new ArrayList<>();
        range.forEach(userIndex -> {
            RankKeyGetDto keyGetDto = findListDto.getRankKeyGetDto();
            String rankKey = redisKeyManager.getRankKeyBySeason(keyGetDto);
            RankRedis userRankInfo = findRank(rankKey, Integer.parseInt(userIndex));
            RedisRankingFindDto findRankingDto = RedisRankingFindDto.builder().rank(userRankInfo).keyGetDto(keyGetDto).build();
            rankList.add(RankUserDto.builder()
                    .intraId(userRankInfo.getIntraId())
                    .ppp(userRankInfo.getPpp())
                    .rank(findRanking(findRankingDto))
                    .statusMessage(userRankInfo.getStatusMessage())
                    .losses(userRankInfo.getLosses())
                    .wins(userRankInfo.getWins())
                    .winRate(userRankInfo.getWinRate())
                    .build());
        });
        return rankList;
    }
}
