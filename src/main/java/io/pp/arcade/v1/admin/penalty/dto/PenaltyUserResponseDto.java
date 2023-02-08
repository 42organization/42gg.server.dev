package io.pp.arcade.v1.admin.penalty.dto;

import io.pp.arcade.v1.admin.penalty.RedisPenaltyUser;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class PenaltyUserResponseDto {
    private String intraId;
    private String reason;
    private LocalDateTime releaseTime;

    public PenaltyUserResponseDto(RedisPenaltyUser penaltyUser) {
        this.intraId = penaltyUser.getIntraId();
        this.reason = penaltyUser.getReason();
        this.releaseTime = penaltyUser.getReleaseTime();
    }
}
