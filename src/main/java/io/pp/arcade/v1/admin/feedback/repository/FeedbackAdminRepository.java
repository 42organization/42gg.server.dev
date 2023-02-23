package io.pp.arcade.v1.admin.feedback.repository;

import io.pp.arcade.v1.domain.feedback.Feedback;
import io.pp.arcade.v1.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackAdminRepository extends JpaRepository<Feedback, Integer> {
    Page<Feedback> findAllByUser(User user, Pageable pageable);
    List<Feedback> findAllByUser(User user);
    @Query(nativeQuery = false, value = "select u from User as u where u.intraId like %:partial%")
    Page<Feedback> findByIntraIdContains(@Param("partial") String partial, Pageable pageable);
}
