package io.pp.arcade.v1.domain.game;

import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.StatusType;
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


    @NotNull
    @Column(name = "season")
    private Integer season;

    @NotNull
    @Setter
    @Column(name = "status")
    private StatusType status;

    @Setter
//    @NotNull
    @Column(name = "mode")
    private Mode mode;

    @Setter
    @OneToOne
    @JoinColumn(name = "team1_id")
    private Team team1;

    @Setter
    @OneToOne
    @JoinColumn(name = "team2_id")
    private Team team2;

    @Setter
    @Column(name = "type")
    private GameType type;

    @Setter
    @Column(name = "time")
    private LocalDateTime time;

    @Builder
    public Game(Slot slot, Integer season, StatusType status, Mode mode, Team team1, Team team2, GameType type, LocalDateTime time) {
        this.slot = slot;
        this.season = season;
        this.status = status;
        this.mode = mode;
        this.team1 = team1;
        this.team2 = team2;
        this.type = type;
        this.time = time;
    }
}
