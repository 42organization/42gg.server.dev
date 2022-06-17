package io.pp.arcade.domain.admin.dto.create;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SlotCreateDto {
    private Integer tableId;
    private Integer team1Id;
    private Integer team2Id;
    private LocalDateTime time;
    private Integer gamePpp;
    private Integer headCount;
    private String type;
}
