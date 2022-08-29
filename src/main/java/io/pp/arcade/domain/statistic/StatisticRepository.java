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
        //String query = queryByDate(dto.getTable(), dto.getDateType(), dto.getStartAt(), dto.getEndAt());
        return jdbcTemplate.query(dto.getQuery(), tableRowMapper());
    }

    @Transactional
    public List<TableMapper> findDataByCreatedAtAndQuery(FindDataDto dto) {
        //String query = queryByDate(dto.getTable(), dto.getDateType(), dto.getStartAt(), dto.getEndAt());
        return jdbcTemplate.query(dto.getQuery(), tableRowMapper());
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
