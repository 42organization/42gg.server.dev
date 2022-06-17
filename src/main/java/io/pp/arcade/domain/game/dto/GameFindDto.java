package io.pp.arcade.domain.game.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Builder
@Getter
public class GameFindDto {
    Integer id;
    String status;
    Pageable pageable;
}
