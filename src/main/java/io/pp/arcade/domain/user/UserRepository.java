package io.pp.arcade.domain.user;

import io.pp.arcade.global.type.RoleType;
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

//    @Query("SELECT u.intraId, u.totalExp, rank() OVER (order by totalExp desc) as MyRank FROM User as u")
// 이렇게 하려고 했으나 레디스에서 totalExp로 순위를 매기고 반환되는 값으로 가져오기,

}
