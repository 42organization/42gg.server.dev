package io.pp.arcade.domain.game.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class GameResultResponseDto {
    List<GameResultDto> games;
    Integer currentPage;
    Integer totalPage;
}
