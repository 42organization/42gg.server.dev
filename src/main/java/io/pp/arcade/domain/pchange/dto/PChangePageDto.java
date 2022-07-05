package io.pp.arcade.domain.pchange.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class PChangePageDto {
    List<PChangeDto> pChangeList;
    Integer currentPage;
    Integer totalPage;
}
