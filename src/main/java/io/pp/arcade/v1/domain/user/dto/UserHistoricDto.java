package io.pp.arcade.v1.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserHistoricDto {
    private Integer ppp;
    private LocalDateTime date;

    @Override
    public String toString() {
        return "UserHistoricDto{" +
                "ppp=" + ppp +
                ", date=" + date +
                '}';
    }
}
