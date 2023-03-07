package io.pp.arcade.v1.admin.pchange.repository;

import io.pp.arcade.v1.domain.pchange.PChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PChangeAdminRepository
        extends JpaRepository<PChange, Integer>{
    @Query(value = "SELECT pc FROM PChange pc join fetch pc.user WHERE pc.user.intraId LIKE %:intraId% order by pc.user.intraId asc, pc.id desc")
    List<PChange> findPChangesByUser_IntraId(@Param("intraId") String intraId);
}
