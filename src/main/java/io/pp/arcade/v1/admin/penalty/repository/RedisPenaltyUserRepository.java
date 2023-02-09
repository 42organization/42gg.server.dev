package io.pp.arcade.v1.admin.penalty.repository;

import io.pp.arcade.v1.admin.penalty.RedisPenaltyUser;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.redis.Key;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisPenaltyUserRepository {
    private final RedisTemplate<String, RedisPenaltyUser> redisTemplate;

    public void addPenaltyUser(RedisPenaltyUser penaltyUser, long penaltyTimeToSecond) {
        redisTemplate.opsForValue().set(Key.PENALTY_USER_ADMIN + penaltyUser.getIntraId(), penaltyUser,
                penaltyTimeToSecond, TimeUnit.SECONDS);
    }

    public RedisPenaltyUser getOneUser(String intraId) {
        return redisTemplate.opsForValue().get(Key.PENALTY_USER_ADMIN + intraId);
    }

    public List<RedisPenaltyUser> getAllPenaltyUser(List<User> allUser) {
        List<RedisPenaltyUser> list = new ArrayList<>();
        for (User user : allUser) {
            RedisPenaltyUser penaltyUser = redisTemplate.opsForValue().get(Key.PENALTY_USER_ADMIN + user.getIntraId());
            if (penaltyUser != null)
                    list.add(penaltyUser);
        }
        return list;
    }

    public void deletePenaltyUser(String intraId) {
        redisTemplate.delete(Key.PENALTY_USER_ADMIN + intraId);
    }
}
