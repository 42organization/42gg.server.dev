package io.pp.arcade.v1.admin.feedback.repository;

import io.pp.arcade.v1.domain.feedback.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface FeedbackAdminRepositorySearch {
    Page<Feedback> findFeedbacksByUserIntraId(@Param("intraId") String intraId, Pageable pageable);
}
