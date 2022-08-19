package io.pp.arcade.domain.slotteamuser;

import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class DeletedSlotTeamUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public DeletedSlotTeamUser(Slot slot, Team team, User user) {
        this.slot = slot;
        this.team = team;
        this.user = user;
    }
}
