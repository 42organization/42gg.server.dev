package io.pp.arcade.v1.domain.slotteamuser.dto;

import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUser;
import io.pp.arcade.v1.domain.team.dto.TeamDto;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlotTeamUserDto {
    private SlotDto slot;
    private TeamDto team;
    private UserDto user;

    static public SlotTeamUserDto from(SlotTeamUser slotTeamUser) {
        return SlotTeamUserDto.builder()
                .slot(SlotDto.from(slotTeamUser.getSlot()))
                .team(TeamDto.from(slotTeamUser.getTeam()))
                .user(UserDto.from(slotTeamUser.getUser()))
                .build();
    }

    @Override
    public String toString() {
        return "SlotTeamUserDto{" +
                "slot=" + slot +
                ", team=" + team +
                ", user=" + user +
                '}';
    }
}
