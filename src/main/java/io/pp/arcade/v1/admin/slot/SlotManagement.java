package io.pp.arcade.v1.admin.slot;

import io.pp.arcade.v1.global.util.BaseTimeEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class SlotManagement extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "past_slot_time")
    private Integer pastSlotTime;

    @NotNull
    @Column(name = "future_slot_time")
    private Integer futureSlotTime;

    @NotNull
    @Column(name = "interval")
    private Integer interval;

    @NotNull
    @Column(name = "open_minute")
    private Integer openMinute;

    public SlotManagement(Integer id, Integer pastSlotTime, Integer futureSlotTime, Integer interval,
                          Integer openMinute) {
        this.id = id;
        this.pastSlotTime = pastSlotTime;
        this.futureSlotTime = futureSlotTime;
        this.interval = interval;
        this.openMinute = openMinute;
    }
}
