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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    //    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @Builder
    public SlotTeamUser(Team team, User user, Slot slot) {
        this.team = team;
        this.user = user;
        this.slot = slot;
        //연관관계 추가
        team.getSlotTeamUserList().add(this);
    }
}
