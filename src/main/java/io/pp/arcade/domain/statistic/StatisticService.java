package io.pp.arcade.domain.statistic;

import io.pp.arcade.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.domain.pchange.PChangeRepository;
import io.pp.arcade.domain.statistic.dto.DateRangeDto;
import io.pp.arcade.domain.statistic.dto.FindDataDto;
import io.pp.arcade.global.type.DateType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticRepository statisticRepository;
    @Transactional
    public List<TableMapper> findVisit(DateType dateType, DateRangeDto dateRangeDto) {
        return null;
    }

    /* 테이블 기준 일주월 조회 */
    @Transactional
    public List<TableMapper> findDataByCreatedAt(FindDataDto dto) {
        return statisticRepository.findDataByCreatedAt(dto);
    }

    /* */
    @Transactional
    public List<TableMapper> findMatchByCreatedAt() {
        return statisticRepository.findTest();
    }

    @Transactional
    public void findData() {
    }




    @Transactional
    public List<TableMapper> findMatch(DateRangeDto dateRangeDto) {
        return null;
    }

}
