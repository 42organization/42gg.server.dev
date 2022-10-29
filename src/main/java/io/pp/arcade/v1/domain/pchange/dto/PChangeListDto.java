package io.pp.arcade.v1.domain.pchange.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PChangeListDto {
    List<PChangeDto> pChangeList;
    Boolean isLast;

    @Override
    public String toString() {
        return "PChangePageDto{" +
                "pChangeList=" + pChangeList +
                ", isLast=" + isLast +
                '}';
    }
}
