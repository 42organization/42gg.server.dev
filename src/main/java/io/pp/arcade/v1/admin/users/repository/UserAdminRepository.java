package io.pp.arcade.v1.admin.users.repository;

import io.pp.arcade.v1.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserAdminRepository extends JpaRepository<User,Integer> {
    Optional<User> findByIntraId(String IntraId);
    @Query(nativeQuery = false, value = "select u from User as u where u.intraId like %:partial%")
    Page<User> findByIntraIdContains(@Param("partial") String partial, Pageable pageable);

}
