package io.pp.arcade.v1.domain.rank.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class RankListResponseDto {
    private Integer myRank;
    private Integer currentPage;
    private Integer totalPage;
    private List<RankUserDto> rankList;

    @Builder
    public RankListResponseDto(Integer myRank, Integer currentPage, Integer totalPage, List<RankUserDto> rankList) {
        this.myRank = myRank;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.rankList = rankList;
    }
}
