package io.pp.arcade.v1.domain.pchange.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PChangePageDto {
    List<PChangeDto> pChangeList;

    @Override
    public String toString() {
        return "PChangePageDto{" +
                "pChangeList=" + pChangeList +
                '}';
    }
}
