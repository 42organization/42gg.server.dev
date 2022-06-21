package io.pp.arcade.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByIntraId(String intraId);
    List<User> findByIntraIdContains(String intraId);
}
