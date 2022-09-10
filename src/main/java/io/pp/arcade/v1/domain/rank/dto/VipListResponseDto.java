package io.pp.arcade.v1.domain.rank.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class VipListResponseDto {
    @Setter
    private Integer myRank;
    private Integer currentPage;
    private Integer totalPage;
    private List<VipUserDto> rankList;

    @Override
    public String toString() {
        return "VipListResponseDto{" +
                ", myRank=" + myRank +
                ", currentPage=" + currentPage +
                ", totalPage=" + totalPage +
                ", rankList=" + rankList +
                '}';
    }
}
