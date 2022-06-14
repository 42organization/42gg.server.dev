package io.pp.arcade.domain.noti;

import io.pp.arcade.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotiRepository extends JpaRepository<Noti, Integer> {
    List<Noti> findAllByUser(User user);
    Integer countAllNByUser(User user);
    void deleteAllByUser(User user);
    void deleteById(Integer id);
}
