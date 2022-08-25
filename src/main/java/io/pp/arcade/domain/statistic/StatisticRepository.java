package io.pp.arcade.domain.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface StatisticRepository {
    List<TableMapper> findAllByDaily();
    List<TableMapper> findAllByWeekly();
    List<TableMapper> findAllByMonthly();
}
