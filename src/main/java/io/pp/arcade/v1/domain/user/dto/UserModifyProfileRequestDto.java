package io.pp.arcade.v1.domain.user.dto;

import io.pp.arcade.v1.global.type.RacketType;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class UserModifyProfileRequestDto {
    @NotNull
    private RacketType racketType;
    @NotNull
    @Size(min = 0, max = 30)
    private String statusMessage;

    @Override
    public String toString() {
        return "UserModifyProfileRequestDto{" +
                "racketType=" + racketType +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }
}

