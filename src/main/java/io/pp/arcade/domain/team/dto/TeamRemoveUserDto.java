package io.pp.arcade.domain.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TeamRemoveUserDto {
    private Integer teamId;
    private Integer userId;
}


