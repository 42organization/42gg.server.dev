package io.pp.arcade.v1.domain.game.dto;

import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.StatusType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Builder
@Getter
public class GameFindDto {
    Integer id;
    Integer seasonId;
    StatusType status;
    Integer count;
    Mode mode;

    @Override
    public String toString() {
        return "GameFindDto{" +
                "id=" + id +
                ", seasonId=" + seasonId +
                ", status=" + status +
                ", count=" + count +
                ", mode=" + mode +
                '}';
    }
}
