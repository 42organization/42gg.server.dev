package io.pp.arcade.v1.admin.noti.repository;

import io.pp.arcade.v1.domain.noti.Noti;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface NotiAdminRepositoryCustom {

    Page<Noti> findNotisByUserIntraId(Pageable pageable, @Param("intraId") String intraId);
}
