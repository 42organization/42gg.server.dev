package io.gg.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserModifyPppRequestDto {
    Integer userId;
    Integer ppp;
}
