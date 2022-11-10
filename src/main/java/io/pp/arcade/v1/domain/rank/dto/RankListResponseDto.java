package io.pp.arcade.v1.domain.rank.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RankListResponseDto {
    private Integer myRanking;
    private Integer currentPage;
    private Integer totalPage;
    private List<RankUserDto> rankList;

    @Builder
    public RankListResponseDto(Integer myRanking, Integer currentPage, Integer totalPage, List<RankUserDto> rankList) {
        this.myRanking = myRanking;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.rankList = rankList;
    }
}
