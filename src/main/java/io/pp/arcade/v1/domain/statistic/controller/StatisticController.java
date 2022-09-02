package io.pp.arcade.v1.domain.statistic.controller;


import io.pp.arcade.v1.domain.statistic.dto.DateRangeDto;
import io.pp.arcade.v1.domain.statistic.dto.StatisticResponseDto;
import io.pp.arcade.v1.global.type.DateType;
import org.springframework.web.bind.annotation.PathVariable;

public interface StatisticController {
    StatisticResponseDto visit(@PathVariable DateType date, DateRangeDto dateRangeDto);
}
