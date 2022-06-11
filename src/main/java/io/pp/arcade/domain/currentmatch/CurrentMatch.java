package io.pp.arcade.domain.currentmatch;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class CurrentMatch extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    @NotNull
    @ManyToOne
    @JoinColumn
    Slot slot;

    @ManyToOne
    @JoinColumn
    Game game;

    @Column(name = "match_imminent")
    Boolean matchImminent;

    @Column(name = "is_matched")
    Boolean isMatched;

    @Builder
    public CurrentMatch(User user, Slot slot, Game game, Boolean matchImminent, Boolean isMatched) {
        this.user = user;
        this.slot = slot;
        this.game = game;
        this.matchImminent = matchImminent;
        this.isMatched = isMatched;
    }
}
