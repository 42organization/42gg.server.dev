package io.pp.arcade.v1.domain.admin.controller;

import io.pp.arcade.v1.domain.admin.dto.CronUpdateDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface SchedulerController {
    void slotGeneratorCronUpdate(@RequestBody CronUpdateDto cron);
    void rankSchedulerUpdate(@RequestBody CronUpdateDto cron);
    void rankDailyProcessNow();
    void gameCronUpdate(@RequestBody CronUpdateDto cron);
    void currentMatchCronUpdate(@RequestBody CronUpdateDto cron);
}
