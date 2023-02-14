package io.pp.arcade.v1.admin.penalty.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PenaltyListResponseDto {
    List<PenaltyUserResponseDto> penaltyList;
    int currentPage;
    int totalPage;
}
