package io.gg.arcade.domains.team.entity;

import io.gg.arcade.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Team")
@NoArgsConstructor
public class Team extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "team_id")
    private Integer teamId;

    @NotNull
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "is_win")
    private Boolean isWin;

    @Column(name = "score")
    private Integer score;

    @Column(name = "ppp_change")
    private Integer pppChange;

    @Column(name = "ppp_result")
    private Integer pppResult;

    @Builder
    public Team(Integer id, Integer teamId, Integer userId, Boolean isWin, Integer score, Integer pppChange, Integer pppResult) {
        this.id = id;
        this.teamId = teamId;
        this.userId = userId;
        this.isWin = isWin;
        this.score = score;
        this.pppChange = pppChange;
        this.pppResult = pppResult;
    }
}
