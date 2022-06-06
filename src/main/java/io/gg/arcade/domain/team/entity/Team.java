package io.gg.arcade.domain.team.entity;

import io.gg.arcade.domain.user.entity.User;
import io.gg.arcade.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor
public class Team extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "temaId")
    private String teamId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(name = "is_win")
    private Boolean isWin;

    @NotNull
    @Column(name = "score")
    private Integer score;

    @NotNull
    @Column(name = "ppp_change")
    private Integer pppChange;

    @NotNull
    @Column(name = "ppp_result")
    private Integer pppResult;

    @Builder
    public Team(String teamId, User user, Boolean isWin, Integer score, Integer pppChange, Integer pppResult) {
        this.teamId = teamId;
        this.user = user;
        this.isWin = isWin;
        this.score = score;
        this.pppChange = pppChange;
        this.pppResult = pppResult;
    }
}
