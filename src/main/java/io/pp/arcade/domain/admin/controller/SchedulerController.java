package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.CronUpdateDto;
import io.pp.arcade.domain.admin.dto.update.SlotGeneratorUpdateDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface SchedulerController {
    void rankSchedulerUpdate(@RequestBody CronUpdateDto cron);
    void gameCronUpdate(@RequestBody CronUpdateDto cron);
    void currentMatchCronUpdate(@RequestBody CronUpdateDto cron);
}
