package io.pp.arcade.v1.domain.statistic;

import io.pp.arcade.v1.domain.statistic.dto.DateRangeDto;
import io.pp.arcade.v1.domain.statistic.dto.FindDataDto;
import io.pp.arcade.v1.global.type.DateType;
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
    public List<TableMapper> findMatchSuccessByCreatedAt() {
        //return statisticRepository.findDataByCreatedAtAndQuery(dto, );
        return null;
    }

    @Transactional
    public void findData() {
    }


    @Transactional
    public List<TableMapper> findMatch(DateRangeDto dateRangeDto) {
        return null;
    }

    private String queryByDate(String table, DateType dateType, String startAt, String endAt){
        String query = null;
        switch (dateType){
            case DAILY:
                query = "select date_format(created_at, '%m-%d') as labels," +
                        " count(*) as data" +
                        " from " + table +
                        " where created_at between " + startAt + " and " + endAt +
                        " group by labels order by labels ASC";
                break;
            case WEEKLY:
                query = "select concat(date_format(created_at, '%m'), '.' ," +
                        " yearweek(created_at, 1) - yearweek(date_sub(created_at, INTERVAL DAYOFMONTH(created_at) - 1 DAY), 1)) as labels," +
                        " count(*) as data" +
                        " from (select date_sub(created_at, interval WEEKDAY(created_at) DAY) as created_at from " + table +
                        " where created_at between " + startAt + " and " + endAt + ") stat " +
                        " group by labels order by labels ASC";
                break;
            case MONTHLY:
                query = "select date_format(created_at, '%m') as labels," +
                        " count(*) as data" +
                        " from " + table +
                        " where created_at between " + startAt + " and " + endAt  +
                        " group by labels order by labels ASC";
                break;
        }
        return query;
    }
}
