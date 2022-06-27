package io.pp.arcade.domain.rank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
public class RankListDto {
    private Integer currentPage;
    private Integer totalPage;
    private List<RankUserDto> rankList;
}
