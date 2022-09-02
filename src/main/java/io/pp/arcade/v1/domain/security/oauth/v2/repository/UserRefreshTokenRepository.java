package io.pp.arcade.v1.domain.security.oauth.v2.repository;

import io.pp.arcade.v1.domain.security.jwt.Token;
import io.pp.arcade.v1.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<Token, Long> {
    Token findByUser(User user);
    Token findByUserAndRefreshToken(User user, String refreshToken);
    Token findByRefreshToken(String token);
}