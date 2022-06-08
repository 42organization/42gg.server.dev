package io.gg.arcade.domain.game.entity;

import io.gg.arcade.common.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Game extends BaseTimeEntity {
    @Id
    @GeneratedValue
    Integer id;

    @Column(name="team1_id")
    String team1Id;

    @Column(name="team2_id")
    String team2Id;

    @Setter
    @Column(name="team1_win")
    Boolean team1Win;

    @Setter
    @Column(name="team1_score")
    Integer team1Score;

    @Setter
    @Column(name="team2_win")
    Boolean team2Win;

    @Setter
    @Column(name="team2_score")
    Integer team2Score;

    @Setter
    @Column(name = "status")
    String gameStatus;

    @Builder
    public Game(String team1Id, Integer team1Score, Boolean team1Win, String team2Id, Integer team2Score, Boolean team2Win, String gameStatus) {
        this.team1Id = team1Id;
        this.team1Score = team1Score;
        this.team1Win = team1Win;
        this.team2Id = team2Id;
        this.team2Score = team2Score;
        this.team2Win = team2Win;
        this.gameStatus = gameStatus;
    }
}
