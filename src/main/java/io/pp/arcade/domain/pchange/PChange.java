package io.pp.arcade.domain.pchange;

import io.pp.arcade.global.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class PChange extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "game_id")
    private Integer gameId;

    @NotNull
    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    @Column(name = "ppp_change")
    private  Integer pppChange;

    @NotNull
    @Column(name = "ppp_result")
    private  Integer pppResult;

    @Builder
    public PChange(Integer gameId, Integer userId, Integer pppChange, Integer pppResult) {
        this.gameId = gameId;
        this.userId = userId;
        this.pppChange = pppChange;
        this.pppResult = pppResult;
    }
}
