package io.pp.arcade.v1.domain.rank;

import io.pp.arcade.v1.domain.rank.dto.RankKeyGetDto;
import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import io.pp.arcade.v1.domain.season.SeasonService;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.GameType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Redis Key Manager
 * @author donghyuk
 */
@Component
@RequiredArgsConstructor
public class RedisKeyManager {
    private final RedisTemplate<String, String> redisTemplate;
    private final SeasonService seasonService;
    private final ListOperations<String, RankRedis> redisRankList;
    public static final String RANK_KEY_DELIMITER = ":";

    public String getRankKeyBySeason(RankKeyGetDto getDto) {
        return getSeasonKey(getDto.getSeasonId().toString(), getDto.getSeasonName());
    }

    public String getRankingKeyBySeason(RankKeyGetDto getDto, GameType gameType) {
        return getRankKeyBySeason(getDto) + RANK_KEY_DELIMITER + gameType.getCode();
    }

    public String getCurrentRankKey() {
        String key = null;
        SeasonDto curSeason =  seasonService.findCurrentRankSeason();
        if (curSeason != null)
            key = getSeasonKey(curSeason.getId().toString(), curSeason.getSeasonName());
        return key;
    }

    public String getCurrentRankingKey(GameType gameType) {
        String key = null;
        SeasonDto curSeason =  seasonService.findCurrentRankSeason();
        if (curSeason != null) {
            RankKeyGetDto keyGetDto = RankKeyGetDto.builder().seasonId(curSeason.getId()).seasonName(curSeason.getSeasonName()).build();
            key = getRankingKeyBySeason(keyGetDto, gameType);
        }
        return key;
    }

    public boolean isEmpty() {
        Set rankKeys = redisTemplate.keys("*");
        return rankKeys.isEmpty();
    }

    public String getSeasonKey(String seasonId, String seasonName) {
        return seasonId + RANK_KEY_DELIMITER + seasonName;
    }

    public Integer getRankIndex(String key, Integer userId)  {
        List<RankRedis> rankRedisList = redisRankList.range(key, 0, -1);

        for (int index = 0; index < rankRedisList.size(); index++) {
            if (rankRedisList.get(index).getId().equals(userId))
                return index;
        }
        return null;
    }
}
