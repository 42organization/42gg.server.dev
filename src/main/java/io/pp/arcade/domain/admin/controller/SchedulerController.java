package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.RankSchedulerModifyDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface SchedulerController {
    void RankSchedulerModify(@RequestBody RankSchedulerModifyDto requestDto);
}
