package io.pp.arcade.domain.game.dto;

import io.pp.arcade.global.type.StatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

@Builder
@Getter
@ToString
public class GameFindDto {
    Integer id;
    StatusType status;
    Pageable pageable;
}
