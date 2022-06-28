package io.pp.arcade.global.scheduler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class SchedularAdminController {
    private final CurrentMatchUpdater currentMatchUpdater;
    private final GameGenerator gameGenerator;

    @PutMapping(value = "/scheduler/current")
    public void currentMatchCronUpdate(@RequestBody CronUpdateDto cron) {
        currentMatchUpdater.setCron(cron.getCron());
        currentMatchUpdater.renewScheduler();
    }

    @PutMapping(value = "/scheduler/game")
    public void gameCronUpdate(@RequestBody CronUpdateDto cron) {
        gameGenerator.setCron(cron.getCron());
        gameGenerator.renewScheduler();
    }
}
