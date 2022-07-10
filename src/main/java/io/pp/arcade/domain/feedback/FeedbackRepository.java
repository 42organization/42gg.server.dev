package io.pp.arcade.domain.feedback;

import io.pp.arcade.global.type.FeedbackType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Page<Feedback> findAllByCategory(String category, Pageable pageable);
    Page<Feedback> findAllByIsSolved(Boolean isSolved, Pageable pageable);
    Page<Feedback> findAllByCategoryAndIsSolved(String category, Boolean isSolved, Pageable pageable);
}
