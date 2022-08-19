package io.pp.arcade.domain.currentmatchhistory;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.type.MatchStatus;
import io.pp.arcade.global.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class CurrentMatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_matched")
    private Boolean isMatched;

    @Column(name = "match_imminent")
    private Boolean matchImminent;

    @JoinColumn(name = "slot_id")
    @ManyToOne
    private Slot slot;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @JoinColumn(name = "game_id")
    @ManyToOne
    private Game game;

    @Column(name = "match_status")
    private MatchStatus matchStatus;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "last_updated_time")
    private LocalDateTime updatedTime;

    @Builder
    public CurrentMatchHistory(Boolean isMatched, Boolean matchImminent, Slot slot, User user, Game game, MatchStatus matchStatus, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.isMatched = isMatched;
        this.matchImminent = matchImminent;
        this.slot = slot;
        this.user = user;
        this.game = game;
        this.matchStatus = matchStatus;
        this.createdTime = createdAt;
        this.updatedTime = modifiedAt;
    }
}
