package io.pp.arcade.v1.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class SlotAddDto {
    private LocalDateTime time;
    private LocalDateTime endTime;
    private Integer tableId;

    @Override
    public String toString() {
        return "SlotAddDto{" +
                "time=" + time +
                ", tableId=" + tableId +
                '}';
    }
}
