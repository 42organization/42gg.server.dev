package io.pp.arcade.domain.team.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamAddUserDto {
    private Integer teamId;
    private Integer userId;
}


