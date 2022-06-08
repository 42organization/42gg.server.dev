package io.gg.arcade.domain.user.repository;

import io.gg.arcade.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByIntraId(String intraId);
}
