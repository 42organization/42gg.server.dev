package io.pp.arcade.v1.admin.penalty.service;

import io.pp.arcade.v1.admin.penalty.RedisPenaltyUser;
import io.pp.arcade.v1.admin.penalty.dto.PenaltyUserResponseDto;
import io.pp.arcade.v1.admin.penalty.repository.RedisPenaltyUserRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.redis.Key;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class PenaltyService {
    private final RedisPenaltyUserRepository redisPenaltyUserRepository;
    private final UserRepository userRepository;

    public void givePenalty(String intraId, int penaltyTime, String reason) {
        userRepository.findByIntraId(intraId).orElseThrow();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime releaseTime = now.plusHours(penaltyTime);
        RedisPenaltyUser penaltyUser = new RedisPenaltyUser(intraId, penaltyTime, releaseTime, reason);
        redisPenaltyUserRepository.addPenaltyUser(penaltyUser);
    }

    public RedisPenaltyUser getOnePenaltyUser(String intraId) {
        return redisPenaltyUserRepository.getOneUser(intraId);
    }
}
