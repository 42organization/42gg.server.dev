package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.CronUpdateDto;
import io.pp.arcade.global.scheduler.CurrentMatchUpdater;
import io.pp.arcade.global.scheduler.GameGenerator;
import io.pp.arcade.global.scheduler.RankScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class SchedulerControllerImpl implements SchedulerController {
    private final RankScheduler rankScheduler;
    private final CurrentMatchUpdater currentMatchUpdater;
    private final GameGenerator gameGenerator;

    @Override
    @PutMapping(value = "/scheduler/rank")
    public void RankSchedulerModify(CronUpdateDto cron) {
        rankScheduler.setCron(cron.getCron());
        rankScheduler.renewScheduler();
    }

    @PutMapping(value = "/scheduler/current")
    public void currentMatchCronUpdate(CronUpdateDto cron) {
        currentMatchUpdater.setCron(cron.getCron());
        currentMatchUpdater.renewScheduler();
    }

    @PutMapping(value = "/scheduler/game")
    public void gameCronUpdate(CronUpdateDto cron) {
        gameGenerator.setCron(cron.getCron());
        gameGenerator.renewScheduler();
    }
}
