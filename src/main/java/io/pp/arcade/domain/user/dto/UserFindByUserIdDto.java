package io.pp.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Builder
@Getter
public class UserFindByUserIdDto {
    Integer userId;
}