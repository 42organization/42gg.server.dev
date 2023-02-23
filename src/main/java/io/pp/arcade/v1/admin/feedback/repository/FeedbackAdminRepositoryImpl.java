package io.pp.arcade.v1.admin.feedback.repository;

import io.pp.arcade.v1.domain.feedback.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class FeedbackAdminRepositoryImpl implements FeedbackAdminRepositorySearch{

    private final EntityManager em;
    @Override
    public Page<Feedback> findFeedbacksByUserIntraId(String intraId, Pageable pageable) {
        long feedbackNum = countTotalFeedbacks(intraId, pageable);
        String sql = "select f from Feedback f where " +
                "f.user.intraId like \'%" + intraId + "%\' order by f.user.intraId asc, f.createdAt asc";
        List<Feedback> feedbackList = em.createQuery(sql, Feedback.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        Page<Feedback> feedbackPage = new PageImpl<>(feedbackList, pageable, feedbackNum);
        return feedbackPage;
    }

    private long countTotalFeedbacks(String intraId, Pageable pageable){
        String sql = "select f from Feedback f where " +
                "f.user.intraId like \'%" + intraId + "%\'";
        List<Feedback> feedbackList = em.createQuery(sql, Feedback.class)
                .getResultList();
        return feedbackList.size();
    }
}
