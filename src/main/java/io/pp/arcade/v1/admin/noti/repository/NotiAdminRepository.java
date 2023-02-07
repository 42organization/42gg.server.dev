package io.pp.arcade.v1.admin.noti.repository;

import io.pp.arcade.v1.domain.noti.Noti;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotiAdminRepository
        extends JpaRepository<Noti, Integer> , NotiAdminRepositoryCustom{

    @Override
    @EntityGraph(attributePaths = {"user", "slot"})
    Page<Noti> findAll(Pageable pageable);

}
