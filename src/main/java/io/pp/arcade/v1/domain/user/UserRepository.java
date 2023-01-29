package io.pp.arcade.v1.domain.user;

import io.pp.arcade.v1.global.type.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByIntraId(String intraId);
    List<User> findByIntraIdContains(String intraId);
    @Query(nativeQuery = false, value = "select u from User as u where u.intraId like %:partial%")
    Page<User> findByIntraIdContains(@Param("partial") String partial, Pageable pageable);
    List<User> findAllByRoleType(RoleType roleType);
    User getUserByIntraId(String IntraId);
    Page<User> findAllByOrderByTotalExpDesc(Pageable pageable);

    @Query(nativeQuery = true, value = "select ranking from (select intra_id, row_number() over (order by total_exp desc) as ranking from user) ranked where intra_id=:intraId")
    Integer findExpRankingByIntraId(@Param("intraId") String intraId);
}
