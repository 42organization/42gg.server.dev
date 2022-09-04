package io.pp.arcade.v1.domain.pchange;

import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "intra_id")
    private User user;

    @Setter
    @NotNull
    @Column(name = "ppp_change")
    private  Integer pppChange;

    @Setter
    @NotNull
    @Column(name = "ppp_result")
    private  Integer pppResult;

    @Setter
//    @NotNull
    @Column(name = "exp_change")
    private Integer expChange;

    @Setter
//    @NotNull
    @Column(name = "exp_result")
    private Integer expResult;

    @Builder
    public PChange(Game game, User user, Integer pppChange, Integer pppResult, Integer expChange, Integer expResult) {
        this.game = game;
        this.user = user;
        this.pppChange = pppChange;
        this.pppResult = pppResult;
        this.expChange = expChange;
        this.expResult = expResult;
    }
}
