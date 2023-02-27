package io.pp.arcade.v1.admin.pchange.repository;

import io.pp.arcade.v1.domain.pchange.PChange;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class PChageAdminRepositorySearchImpl implements PChangeAdminRepositorySearch{

    private final EntityManager em;
    @Override
    public List<PChange> findPChangesByUserIntraId(String intraId, Pageable pageable) {
        String sql = "select pc from PChange pc join fetch pc.user where " +
                "pc.user.intraId like \'%" + intraId + "%\' order by pc.user.id asc, pc.createdAt desc";
        List<PChange> pChangeList = em.createQuery(sql, PChange.class)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        return pChangeList;
    }
}
