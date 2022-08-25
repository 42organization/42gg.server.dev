package io.pp.arcade.domain.statistic.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class DataSet {
    private String label;
    private List<Integer> data;
}
