package io.pp.arcade.v1.admin.service;

import io.pp.arcade.v1.domain.rank.RankRedisRepository;
import io.pp.arcade.v1.domain.rank.RedisKeyManager;
import io.pp.arcade.v1.domain.rank.dto.RankDto;
import io.pp.arcade.v1.domain.rank.dto.RankKeyGetDto;
import io.pp.arcade.v1.domain.rank.dto.RedisRankAddDto;
import io.pp.arcade.v1.domain.rank.dto.RedisRankingAddDto;
import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import io.pp.arcade.v1.domain.season.SeasonRepository;
import io.pp.arcade.v1.domain.season.SeasonService;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.domain.user.UserService;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminRankRedisService {
    private final UserRepository userRepository;
    private final RedisKeyManager redisKeyManager;
    private final RankRedisRepository rankRedisRepository;
    @Transactional
    public void addAllUserRankByNewSeason(SeasonDto seasonDto, Integer startPpp) {
        List<User> users = userRepository.findAll();
        RankKeyGetDto rankKeyGetDto = RankKeyGetDto.builder().seasonId(seasonDto.getId()).seasonName(seasonDto.getSeasonName()).build();
        String curRankKey = redisKeyManager.getRankKeyBySeason(rankKeyGetDto);
        String curRankingKey = redisKeyManager.getRankingKeyBySeason(rankKeyGetDto, GameType.SINGLE);

        users.forEach(user -> {
            UserDto userDto = UserDto.from(user);
            RankRedis userRank = RankRedis.from(userDto, GameType.SINGLE);
            user.setPpp(startPpp);
            userRank.setPpp(startPpp);
            RedisRankAddDto redisRankAddDto = RedisRankAddDto.builder().key(curRankKey).userId(userDto.getId()).rank(userRank).build();
            rankRedisRepository.addRank(redisRankAddDto);

            RedisRankingAddDto redisRankingAddDto = RedisRankingAddDto.builder().rankingKey(curRankingKey).rank(userRank).build();
            rankRedisRepository.addRanking(redisRankingAddDto);
        });
    }
    @Transactional
    public List<RankDto> findRankByAdmin(Pageable pageable) {
        return null;
    }
}
