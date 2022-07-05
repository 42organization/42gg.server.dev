package io.pp.arcade.domain.currentmatch.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
public class CurrentMatchResponseDto {
    private LocalDateTime time;
    private Integer slotId;
    private List<String> myTeam;
    private List<String> enemyTeam;
    private Boolean isMatched;
}
