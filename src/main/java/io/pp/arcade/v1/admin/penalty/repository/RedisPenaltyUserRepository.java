package io.pp.arcade.v1.admin.penalty.repository;

import io.pp.arcade.v1.admin.penalty.RedisPenaltyUser;
import io.pp.arcade.v1.global.redis.Key;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisPenaltyUserRepository {
    private final RedisTemplate<String, RedisPenaltyUser> redisTemplate;

    public void addPenaltyUser(RedisPenaltyUser penaltyUser) {
        redisTemplate.opsForValue().set(Key.PENALTY_USER_ADMIN + penaltyUser.getIntraId(), penaltyUser,
                penaltyUser.getPenaltyTime(), TimeUnit.HOURS);
    }

    public RedisPenaltyUser getOneUser(String intraId) {
        return redisTemplate.opsForValue().get(Key.PENALTY_USER_ADMIN + intraId);
    }
}
