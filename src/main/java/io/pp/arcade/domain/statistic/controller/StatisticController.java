package io.pp.arcade.domain.statistic.controller;


import io.pp.arcade.domain.statistic.dto.DateRangeDto;
import io.pp.arcade.domain.statistic.dto.StatisticResponseDto;
import io.pp.arcade.global.type.DateType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public interface StatisticController {
    StatisticResponseDto visit(@PathVariable DateType date, DateRangeDto dateRangeDto);
}
