package io.gg.arcade.domain.team.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamRemoveUserRequestDto {
    String teamId;
    Integer userId;
}
