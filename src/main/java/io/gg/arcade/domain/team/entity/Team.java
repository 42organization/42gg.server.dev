package io.gg.arcade.domain.team.entity;

import io.gg.arcade.domain.user.entity.User;
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
    Integer id;

    @NotNull
    @Column(name="team_id")
    String teamId;

    @Setter
    @ManyToOne
    @JoinColumn(name="user1_id")
    User user1;

    @Setter
    @ManyToOne
    @JoinColumn(name="user2_id")
    User user2;

    @Setter
    @Column(name = "team_ppp")
    Integer teamPpp;

    @Setter
    @Column(name = "headCount")
    Integer headCount;

    @Builder
    public Team(String teamId, User user1, User user2, Integer teamPpp, Integer headCount) {
        this.teamId = teamId;
        this.user1 = user1;
        this.user2 = user2;
        this.headCount = headCount;
        this.teamPpp = teamPpp;
    }
}