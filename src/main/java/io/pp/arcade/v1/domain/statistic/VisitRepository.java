package io.pp.arcade.v1.domain.statistic;

import io.pp.arcade.v1.domain.statistic.dto.DateRangeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

    @Query(nativeQuery = true, value = "select * from user")
    List<TableMapper> findByCreatedAt(DateRangeDto queryDto);
}
