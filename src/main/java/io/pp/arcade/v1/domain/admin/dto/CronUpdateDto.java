package io.pp.arcade.v1.domain.admin.dto;

import lombok.Getter;

@Getter
public class CronUpdateDto {
    private String cron;
    private Integer interval;
}
