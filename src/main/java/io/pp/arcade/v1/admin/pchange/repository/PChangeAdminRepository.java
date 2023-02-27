package io.pp.arcade.v1.admin.pchange.repository;

import io.pp.arcade.v1.domain.pchange.PChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PChangeAdminRepository extends JpaRepository<PChange, Integer> {
    List<PChange> findPChangesByUser_IntraId(@Param("q") String intraId);
}
