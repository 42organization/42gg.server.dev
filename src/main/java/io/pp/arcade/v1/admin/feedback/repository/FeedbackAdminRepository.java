package io.pp.arcade.v1.admin.feedback.repository;

import io.pp.arcade.v1.domain.feedback.Feedback;
import io.pp.arcade.v1.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackAdminRepository extends JpaRepository<Feedback, Integer> {
    Page<Feedback> findAllByUser(User user, Pageable pageable);
}
