package io.pp.arcade.v1.global.scheduler;

import io.pp.arcade.v1.domain.rank.service.RankService;
import io.pp.arcade.v1.domain.season.SeasonService;
import org.springframework.stereotype.Component;

@Component
public class RankScheduler extends  AbstractScheduler {
    private final SeasonService seasonService;
    private final RankService rankService;

    public RankScheduler(RankService rankService, SeasonService seasonService) {
        this.rankService = rankService;
        this.seasonService = seasonService;
        this.setCron("0 55 23 * * *");
        this.setInterval(0);
    }

    public void dailyProcess() {
        /*
        List<RankUserDto> rankRedisDtos = rankRedisService.findCurrentRankList();
        SeasonDto season = seasonService.findLatestRankSeason();
        RankSaveAllDto rankSaveAllDto = RankSaveAllDto.builder().rankUserDtos(rankRedisDtos).seasonDto(season).build();
        rankService.redisToMySql(rankSaveAllDto);
        */
    }

    @Override
    public Runnable runnable() {
        return () -> {
            dailyProcess();
        };
    }
}
