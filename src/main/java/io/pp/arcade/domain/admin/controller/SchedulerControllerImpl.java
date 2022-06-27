package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.RankSchedulerModifyDto;
import io.pp.arcade.global.scheduler.RankScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class SchedulerControllerImpl implements SchedulerController {
    private final RankScheduler rankScheduler;

    @Override
    @PostMapping(value = "/scheduler/rank")
    public void RankSchedulerModify(RankSchedulerModifyDto requestDto) {
        rankScheduler.stopScheduler();
        rankScheduler.setCron(requestDto.getCron());
        rankScheduler.startScheduler();
    }
}
