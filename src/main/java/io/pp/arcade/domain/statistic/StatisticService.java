package io.pp.arcade.domain.statistic;

import io.pp.arcade.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.domain.pchange.PChangeRepository;
import io.pp.arcade.domain.statistic.dto.DateRangeDto;
import io.pp.arcade.global.type.DateType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {

    @Transactional
    public List<TableMapper> findVisit(DateType dateType, DateRangeDto dateRangeDto) {
        return null;
    }

    @Transactional
    public List<TableMapper> findMatch(DateRangeDto dateRangeDto) {
        return null;
    }
}
