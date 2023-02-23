package io.pp.arcade.v1.admin.feedback.repository;

import io.pp.arcade.v1.domain.feedback.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FeedbackAdminRepository extends JpaRepository<Feedback, Integer>, FeedbackAdminRepositorySearch {

}
