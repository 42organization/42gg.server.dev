package io.gg.arcade.domain.game.entity;

import io.gg.arcade.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    Integer team1Id;

    @Column(name="team1_score")
    Integer team1Score;

    @Column(name="team1_win")
    Boolean team1Win;
    
    @Column(name="team2_id")
    Integer team2Id;

    @Column(name="team2_score")
    Integer team2Score;

    @Column(name="team2_win")
    Boolean team2Win;

    @Builder
    public Game(Integer team1Id, Integer team1Score, Boolean team1Win, Integer team2Id, Integer team2Score, Boolean team2Win) {
        this.team1Id = team1Id;
        this.team1Score = team1Score;
        this.team1Win = team1Win;
        this.team2Id = team2Id;
        this.team2Score = team2Score;
        this.team2Win = team2Win;
    }
}
