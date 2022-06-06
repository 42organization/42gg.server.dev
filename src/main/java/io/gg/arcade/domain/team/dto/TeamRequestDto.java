package io.gg.arcade.domain.team.dto;

import io.gg.arcade.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TeamRequestDto {
    private String teamId;
    private User user;

    @Builder
    public TeamRequestDto(String teamId, User user) {
        this.teamId = teamId;
        this.user = user;
    }

}
