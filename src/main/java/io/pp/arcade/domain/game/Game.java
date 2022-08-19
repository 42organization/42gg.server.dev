package io.pp.arcade.domain.game;

import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.Mode;
import io.pp.arcade.global.type.StatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "slot_id")
    Slot slot;

    @OneToOne
    @JoinColumn(name = "team1_id")
    private Team team1;

    @OneToOne
    @JoinColumn(name = "team2_id")
    private Team team2;

    @Column(name = "type")
    private GameType type;

    @Column(name = "time")
    private LocalDateTime time;

    @NotNull
    @Column(name = "season")
    private Integer season;

    @NotNull
    @Setter
    @Column(name = "status")
    private StatusType status;

    @NotNull
    @Setter
    @Column(name = "mode")
    private Mode mode;
    @Builder
    public Game(Slot slot, Team team1, Team team2, GameType type, LocalDateTime time, Integer season, StatusType status, Mode mode) {
        this.slot = slot;
        this.team1 = team1;
        this.team2 = team2;
        this.type = type;
        this.time = time;
        this.season = season;
        this.status = status;
        this.mode = mode;
    }
}
