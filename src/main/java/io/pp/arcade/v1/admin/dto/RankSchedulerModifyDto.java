package io.pp.arcade.v1.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankSchedulerModifyDto {
    private String cron;
}
