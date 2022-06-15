package io.pp.arcade.domain.rank;

import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.util.RacketType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "ppp")
    private Integer ppp;

    @Column(name = "season_id")
    private Integer seasonId;

    @Column(name = "ranking")
    private Integer ranking;

    @Column(name = "type")
    private RacketType racketType;

    @Column(name = "wins")
    private Integer wins;

    @Column(name = "losses")
    private Integer losses;
}
