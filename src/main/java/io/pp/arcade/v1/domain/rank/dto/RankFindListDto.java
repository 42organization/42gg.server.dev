package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.global.type.GameType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@Builder
public class RankFindListDto {
    private Pageable pageable;
    private GameType gameType;
    private Integer seasonId;
    private Integer count;

    @Builder
    public RankFindListDto(Pageable pageable, GameType gameType, Integer seasonId, Integer count) {
        this.pageable = pageable;
        this.gameType = gameType;
        this.seasonId = seasonId;
        this.count = count;
    }
}
