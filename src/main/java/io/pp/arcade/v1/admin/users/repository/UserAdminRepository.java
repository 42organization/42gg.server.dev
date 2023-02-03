package io.pp.arcade.v1.admin.users.repository;

import io.pp.arcade.v1.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAdminRepository extends JpaRepository<User,Integer> {
    Optional<User> findByIntraId(String IntraId);
}
