package io.pp.arcade.v1.domain.currentmatch.dto;

import io.pp.arcade.v1.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentMatchFindByUserDto {
    private UserDto user;
}
