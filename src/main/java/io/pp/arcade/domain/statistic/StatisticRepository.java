package io.pp.arcade.domain.statistic;

import io.pp.arcade.domain.statistic.dto.FindDataDto;
import io.pp.arcade.global.type.DateType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class StatisticRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public List<TableMapper> findDataByCreatedAt(FindDataDto dto) {
        String query = queryByDate(dto.getTable(), dto.getDateType(), dto.getStartAt(), dto.getEndAt());
        return jdbcTemplate.query(query, tableRowMapper());
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

    private RowMapper<TableMapper> tableRowMapper(){
        return (rs, rowNum) -> {
            TableMapper tableMapper = new TableMapper();
            tableMapper.setData(rs.getInt("data"));
            tableMapper.setLabels(rs.getString("labels"));
            return tableMapper;
        };
    }
}
