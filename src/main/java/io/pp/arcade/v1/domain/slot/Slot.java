package io.pp.arcade.v1.domain.slot;

import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.util.BaseTimeEntity;
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
public class Slot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "table_id")
    private Integer tableId;

    @NotNull
    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Setter
    @Column(name = "game_ppp")
    private Integer gamePpp;

    @NotNull
    @Setter
    @Column(name = "head_count")
    private Integer headCount;

    @Setter
    @Column(name = "type")
    private GameType type;
    @Setter
//    @NotNull
    @Column(name = "mode")
    private Mode mode;

    @Builder
    public Slot(Integer tableId, LocalDateTime time, LocalDateTime endTime,
                Integer gamePpp, Integer headCount, GameType type, Mode mode) {
        this.tableId = tableId;
        this.time = time;
        this.endTime = endTime;
        this.gamePpp = gamePpp;
        this.headCount = headCount;
        this.type = type;
        this.mode = mode;
    }
}
