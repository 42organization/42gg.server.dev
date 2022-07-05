package io.pp.arcade.domain.user.dto;

import io.pp.arcade.global.type.RacketType;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@ToString
public class UserModifyProfileRequestDto {
    @NotNull
    private RacketType racketType;
    @NotNull
    @Size(min = 0, max = 30)
    private String statusMessage;
}

