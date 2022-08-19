package io.pp.arcade.domain.slotteamuser;

import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class SlotTeamUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teamUserId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @Builder
    public SlotTeamUser(Team team, User user, Slot slot) {
        this.team = team;
        this.user = user;
        this.slot = slot;
    }
}
