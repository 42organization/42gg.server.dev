package io.pp.arcade.global.scheduler;

import io.pp.arcade.domain.rank.dto.RankRedisDto;
import io.pp.arcade.domain.rank.dto.RankSaveAllDto;
import io.pp.arcade.domain.rank.service.RankRedisService;
import io.pp.arcade.domain.rank.service.RankService;
import io.pp.arcade.domain.season.SeasonService;
import lombok.Setter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Component
public class RankScheduler {
    private ThreadPoolTaskScheduler scheduler;
    private final RankRedisService rankRedisService;
    private final SeasonService seasonService;
    private final RankService rankService;
    @Setter
    private String cron = "0 55 23 * * *"; // 초 분 시 일 월 년 요일

    public RankScheduler(RankRedisService rankRedisService, RankService rankService, SeasonService seasonService) {
        this.rankRedisService = rankRedisService;
        this.rankService = rankService;
        this.seasonService = seasonService;
    }

    public void dailyProcess() {
        List<RankRedisDto> rankRedisDtos = rankRedisService.findRankAll();
        Integer seasonId = seasonService.findCurrentSeason().getId();
        RankSaveAllDto rankSaveAllDto = RankSaveAllDto.builder().rankRedisDtos(rankRedisDtos).seasonId(seasonId).build();
        rankService.saveAll(rankSaveAllDto);
    }

    public void startScheduler() {
        scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        scheduler.schedule(getDoSomething(), new CronTrigger(cron));
    }

    private Runnable getDoSomething() {
        return () -> {
            dailyProcess();
        };
    }

    public void stopScheduler() {
        scheduler.shutdown();
    }

    @PostConstruct
    public void init(){
        startScheduler();
    }

    @PreDestroy
    public void destroy() {
        stopScheduler();
    }
}
