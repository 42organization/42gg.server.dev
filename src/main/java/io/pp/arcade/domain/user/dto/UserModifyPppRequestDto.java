package io.pp.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserModifyPppRequestDto {
    private Integer userId;
    private Integer ppp;
}
