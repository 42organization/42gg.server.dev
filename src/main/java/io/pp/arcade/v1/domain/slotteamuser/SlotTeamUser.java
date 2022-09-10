package io.pp.arcade.v1.domain.slotteamuser;

import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.util.BaseTimeEntity;
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

//    @NotNull
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    //    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //    @NotNull
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
