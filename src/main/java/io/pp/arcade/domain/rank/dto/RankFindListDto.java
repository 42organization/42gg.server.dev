package io.pp.arcade.domain.rank.dto;

import io.pp.arcade.global.type.GameType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

@Getter
@Builder
@ToString
public class RankFindListDto {
    private Pageable pageable;
    private GameType gameType;
    private Integer count;
}
