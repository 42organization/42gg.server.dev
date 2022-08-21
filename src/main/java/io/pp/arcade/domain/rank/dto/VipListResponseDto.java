package io.pp.arcade.domain.rank.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class VipListResponseDto {
    private String myIntraId;
    @Setter
    private Integer myRank;
    private Integer currentPage;
    private Integer totalPage;
    private List<VipUserDto> vipList;

    @Override
    public String toString() {
        return "VipListResponseDto{" +
                "myIntraId='" + myIntraId + '\'' +
                ", myRank=" + myRank +
                ", currentPage=" + currentPage +
                ", totalPage=" + totalPage +
                ", vipList=" + vipList +
                '}';
    }
}
