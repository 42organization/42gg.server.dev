package io.pp.arcade.v1.admin.pchange.repository;

import io.pp.arcade.v1.domain.pchange.PChange;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PChangeAdminRepositorySearch {
    List<PChange> findPChangesByUserIntraId(@Param("intraId")String intraId, Pageable pageable);
}
