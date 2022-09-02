package io.pp.arcade.v1.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserModifyExpDto {
    private Integer userId;
    private Integer exp;
}
