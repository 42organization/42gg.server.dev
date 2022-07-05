package io.pp.arcade.domain.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TeamAddUserDto {
    private Integer teamId;
    private Integer userId;
}


