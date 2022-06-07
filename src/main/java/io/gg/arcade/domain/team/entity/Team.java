package io.gg.arcade.domain.team.entity;

import io.gg.arcade.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name="user_id")
    User user;

    @Builder
    public Team(String teamId, User user) {
        this.teamId = teamId;
        this.user = user;
    }
}