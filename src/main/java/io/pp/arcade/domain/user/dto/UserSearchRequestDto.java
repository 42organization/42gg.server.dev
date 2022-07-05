package io.pp.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@ToString
public class UserSearchRequestDto {
    @NotNull(message = "")
    String intraId;
}
