package io.pp.arcade.v1.domain.currentmatch.dto;

import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CurrentMatchResponseDto {
    private LocalDateTime time;
    private Integer slotId;
    private List<String> myTeam;
    private List<String> enemyTeam;
    private Boolean isMatched;

    private Mode mode;

    @Override
    public String toString() {
        return "CurrentMatchResponseDto{" +
                "time=" + time +
                ", slotId=" + slotId +
                ", myTeam=" + myTeam +
                ", enemyTeam=" + enemyTeam +
                ", isMatched=" + isMatched +
                ", mode=" + mode +
                '}';
    }
}
