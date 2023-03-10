package io.pp.arcade.v1.admin.rank.service;

import io.pp.arcade.v1.admin.season.service.SeasonAdminService;
import io.pp.arcade.v1.domain.rank.RankRedisRepository;
import io.pp.arcade.v1.domain.rank.RedisKeyManager;
import io.pp.arcade.v1.domain.rank.dto.*;
import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankRedisAdminService {
    private final UserRepository userRepository;
    private final RedisKeyManager redisKeyManager;
    private final RankRedisRepository rankRedisRepository;
    private final SeasonAdminService seasonAdminService;
    private final RedisTemplate<String, String> redisRank;
    @Transactional
    public void addAllUserRankByNewSeason(SeasonDto seasonDto, Integer startPpp) {
        List<User> users = userRepository.findAll();
        RankKeyGetDto rankKeyGetDto = RankKeyGetDto.builder().seasonId(seasonDto.getId()).seasonName(seasonDto.getSeasonName()).build();
        String curRankKey = redisKeyManager.getRankKeyBySeason(rankKeyGetDto);
        String curRankingKey = redisKeyManager.getRankingKeyBySeason(rankKeyGetDto, GameType.SINGLE);

        users.forEach(user -> {
            UserDto userDto = UserDto.from(user);
            RankRedis userRank = RankRedis.from(userDto, GameType.SINGLE);
            RedisRankAddDto redisRankAddDto = RedisRankAddDto.builder().key(curRankKey).userId(userDto.getId()).rank(userRank).build();
            rankRedisRepository.addRank(redisRankAddDto);

            RedisRankingAddDto redisRankingAddDto = RedisRankingAddDto.builder().rankingKey(curRankingKey).rank(userRank).build();
            rankRedisRepository.addRanking(redisRankingAddDto);
        });
    }

    @Transactional
    public void deleteSeasonRankBySeasonId(Integer seasonId) {
        rankRedisRepository.deleteRankSeason(seasonId.toString());
    }
}
