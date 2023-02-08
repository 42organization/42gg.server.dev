package io.pp.arcade.v1.admin.penalty.dto;

import lombok.Getter;

@Getter
public class PenaltyAddRequestDto {
    private String intraId;
    private int penaltyTime;
    private String reason;
}
