package io.pp.arcade.v1.global.scheduler;

import io.pp.arcade.v1.domain.rank.dto.RankSaveAllDto;
import io.pp.arcade.v1.domain.rank.dto.RankUserDto;
import io.pp.arcade.v1.domain.rank.service.RankRedisService;
import io.pp.arcade.v1.domain.rank.service.RankService;
import io.pp.arcade.v1.domain.season.SeasonService;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RankScheduler extends  AbstractScheduler {
    private final RankRedisService rankRedisService;
    private final SeasonService seasonService;
    private final RankService rankService;

    public RankScheduler(RankRedisService rankRedisService, RankService rankService, SeasonService seasonService) {
        this.rankRedisService = rankRedisService;
        this.rankService = rankService;
        this.seasonService = seasonService;
        this.setCron("0 55 23 * * *");
        this.setInterval(0);
    }

    public void dailyProcess() {
        List<RankUserDto> rankRedisDtos = rankRedisService.findCurrentRankList();
        SeasonDto season = seasonService.findCurrentRankSeason();
        RankSaveAllDto rankSaveAllDto = RankSaveAllDto.builder().rankUserDtos(rankRedisDtos).seasonDto(season).build();
        rankService.redisToMySql(rankSaveAllDto);
    }

    @Override
    public Runnable runnable() {
        return () -> {
            dailyProcess();
        };
    }
}
