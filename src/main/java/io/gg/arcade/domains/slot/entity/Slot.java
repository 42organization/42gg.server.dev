package io.gg.arcade.domains.slot.entity;

import io.gg.arcade.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Slot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer slotId;

    @NotNull
    @Column(name = "team1_id")
    private String team1Id;

    @NotNull
    @Column(name = "team2_id")
    private String team2Id;

    @NotNull
    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "game_ppp")
    private Integer gamePpp;

    @Column(name = "head_count")
    private Integer headCount;

    @Column(name = "type")
    private String type;

    @Builder
    public Slot(Integer slotId, String team1Id, String team2Id, LocalDateTime time, Integer gamePpp, Integer headCount, String type) {
        this.slotId = slotId;
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.time = time;
        this.gamePpp = gamePpp;
        this.headCount = headCount;
        this.type = type;
    }
}
