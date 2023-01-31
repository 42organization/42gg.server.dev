package io.pp.arcade.v1.admin.dto;

import lombok.Getter;

@Getter
public class CronUpdateDto {
    private String cron;
    private Integer interval;
}
