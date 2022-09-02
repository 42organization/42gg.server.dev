package io.pp.arcade.v1.domain.statistic.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StatisticResponseDto {
    private List<String> labels;
    private List<DataSet> datasets;
}
