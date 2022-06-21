package io.pp.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
public class UserSearchRequestDto {
    @NotNull(message = "")
    String intraId;
}
