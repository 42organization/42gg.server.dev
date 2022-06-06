package io.gg.arcade.domain.user.repository;

import io.gg.arcade.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
