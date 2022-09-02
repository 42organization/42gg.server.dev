package io.pp.arcade.v1.domain.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankSchedulerModifyDto {
    private String cron;
}
