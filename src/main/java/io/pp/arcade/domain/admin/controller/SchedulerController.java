package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.CronUpdateDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface SchedulerController {
    void RankSchedulerModify(@RequestBody CronUpdateDto cron);
    void gameCronUpdate(@RequestBody CronUpdateDto cron);
    void currentMatchCronUpdate(@RequestBody CronUpdateDto cron);
}
