package io.gg.arcade.domain.team.dto;

import io.gg.arcade.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamRequestDto {
    private String teamId;
    private User user;
}
