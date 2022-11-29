package io.pp.arcade.v1.domain.admin.service;

import io.pp.arcade.v1.domain.rank.RankRepository;
import io.pp.arcade.v1.domain.rank.RedisKeyManager;
import io.pp.arcade.v1.domain.rank.dto.RankDto;
import io.pp.arcade.v1.domain.rank.dto.RankKeyGetDto;
import io.pp.arcade.v1.domain.rank.entity.Rank;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminRankRedisService {
    private final UserRepository userRepository;
    private final RedisKeyManager redisKeyManager;
    private final RankRepository rankRepository;
    @Transactional
    public void addAllUserRankByNewSeason(SeasonDto seasonDto, Integer startPpp) {
        List<User> users = userRepository.findAll();
        RankKeyGetDto rankKeyGetDto = RankKeyGetDto.builder().seasonId(seasonDto.getId()).seasonName(seasonDto.getSeasonName()).build();
        String curRankKey = redisKeyManager.getRankKeyBySeason(rankKeyGetDto);
        String curRankingKey = redisKeyManager.getRankingKeyBySeason(rankKeyGetDto, GameType.SINGLE);

        List<Rank> ranks = new ArrayList<>();
        users.forEach(user -> {
            UserDto userDto = UserDto.from(user);
            Rank userRank = Rank.builder()
                    .ranking(0)
                    .racketType(userDto.getRacketType())
                    .seasonId(seasonDto.getId())
                    .wins(0)
                    .losses(0)
                    .ppp(startPpp)
                    .gameType(GameType.SINGLE)
                    .statusMessage(userDto.getStatusMessage())
                    .build();
            user.setPpp(startPpp);
            ranks.add(userRank);
        });
        rankRepository.saveAll(ranks);
    }
    @Transactional
    public List<RankDto> findRankByAdmin(Pageable pageable) {
        return null;
    }
}
