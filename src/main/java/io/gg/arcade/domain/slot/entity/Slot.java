package io.gg.arcade.domain.slot.entity;

import io.gg.arcade.common.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Slot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "team1_id")
    private String team1Id;

    @NotNull
    @Column(name = "team2_id")
    private String team2Id;

    @NotNull
    @Column(name = "time")
    private LocalDateTime time;

    @Setter
    @Column(name = "game_ppp")
    private Integer gamePpp;

    @Setter
    @NotNull
    @Column(name = "head_count")
    private Integer headCount;

    @Setter
    @Column(name = "type")
    private String type;

    @Builder
    public Slot(String team1Id, String team2Id, LocalDateTime time, Integer gamePpp, Integer headCount, String type) {
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.time = time;
        this.gamePpp = gamePpp;
        this.headCount = headCount;
        this.type = type;
    }
}
