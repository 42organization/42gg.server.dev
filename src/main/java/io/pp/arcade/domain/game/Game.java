package io.pp.arcade.domain.game;

import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.team.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
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
    @OneToOne
    @JoinColumn(name = "team1_id")
    private Team team1;

    @NotNull
    @OneToOne
    @JoinColumn(name = "team2_id")
    private Team team2;

    @NotNull
    @Column(name = "type")
    private String type;

    @NotNull
    @Column(name = "time")
    private LocalDateTime time;

    @NotNull
    @Column(name = "season")
    private Integer season;

    @NotNull
    @Column(name = "status")
    @Setter
    private String status;

    @Builder
    public Game(Slot slot, Team team1, Team team2, String type, LocalDateTime time, Integer season, String status) {
        this.slot = slot;
        this.team1 = team1;
        this.team2 = team2;
        this.type = type;
        this.time = time;
        this.season = season;
        this.status = status;
    }
}
