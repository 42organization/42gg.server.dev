package io.pp.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Builder
@Getter
@ToString
public class UserFindByUserIdDto {
    Integer userId;
}