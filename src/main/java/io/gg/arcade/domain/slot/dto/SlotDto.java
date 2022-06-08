package io.gg.arcade.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Builder
public class SlotDto {
    private Integer id;
    private String team1Id;
    private String team2Id;
    private LocalDateTime time;
    private Integer gamePpp;
    private Integer headCount;
    private String type;
}
