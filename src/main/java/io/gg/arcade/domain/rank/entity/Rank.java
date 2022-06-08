package io.gg.arcade.domain.rank.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
@Table(name="ping_pong_rank")
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    @Column(name = "ppp")
    private Integer ppp;

    @Column(name = "season")
    private Integer season;

    @NotNull
    @Column(name = "ranking")
    private Integer ranking;

    @NotNull
    @Column(name = "type")
    private String type;

    @Builder
    public Rank(Integer userId, Integer ppp, Integer season, Integer ranking, String type) {
        this.userId = userId;
        this.ppp = ppp;
        this.season = season;
        this.ranking = ranking;
        this.type = type;
    }
}
