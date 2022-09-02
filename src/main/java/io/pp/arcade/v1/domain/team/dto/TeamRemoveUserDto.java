package io.pp.arcade.v1.domain.team.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamRemoveUserDto {
    private Integer slotId;
    private Integer userId;

    @Override
    public String toString() {
        return "TeamRemoveUserDto{" +
                "slotId=" + slotId +
                ", userId=" + userId +
                '}';
    }
}


