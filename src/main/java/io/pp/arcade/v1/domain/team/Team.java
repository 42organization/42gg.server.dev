package io.pp.arcade.v1.domain.team;

import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @NotNull
    @Setter
    @Column(name = "team_ppp")
    private Integer teamPpp;

    @NotNull
    @Setter
    @Column(name = "head_count")
    private Integer headCount;

    @NotNull
    @Setter
    @Column(name = "score")
    private Integer score;

    @Setter
    @Column(name = "win")
    private Boolean win;

    @Setter
    @OneToOne
    @JoinColumn(name = "user1_id", referencedColumnName = "intra_id")
    private User user1;

    @Setter
    @OneToOne
    @JoinColumn(name = "user2_id", referencedColumnName = "intra_id")
    private User user2;

    @Builder
    public Team(User user1, User user2, Slot slot, Integer teamPpp, Integer headCount, Integer score, Boolean win) {
        this.user1 = user1;
        this.user2 = user2;
        this.slot = slot;
        this.teamPpp = teamPpp;
        this.headCount = headCount;
        this.score = score;
        this.win = win;
    }
}
