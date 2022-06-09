package io.pp.arcade.domain.team.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamModifyUserRequestDto {
    private Integer teamId;
    private Integer userId;
}
