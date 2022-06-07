package io.gg.arcade.domain.pchange.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class PChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "game_id")
    Integer gameId;

    @Column(name = "user_id")
    Integer userId;

    @Column(name = "ppp_change")
    Integer pppChange;

    @Column(name = "ppp_result")
    Integer pppResult;

    @Builder
    public PChange(Integer gameId, Integer userId, Integer pppChange, Integer pppResult) {
        this.gameId = gameId;
        this.userId = userId;
        this.pppChange = pppChange;
        this.pppResult = pppResult;
    }
}
