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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PenaltyService {
    private final RedisPenaltyUserRepository redisPenaltyUserRepository;
    private final UserRepository userRepository;

    public void givePenalty(String intraId, int penaltyTime, String reason) {
        userRepository.findByIntraId(intraId).orElseThrow();
        RedisPenaltyUser existUser = redisPenaltyUserRepository.getOneUser(intraId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime releaseTime;
        long penaltyTimeToSecond;
        RedisPenaltyUser penaltyUser;
        if (existUser != null) {
            Duration duration = Duration.between(now, existUser.getReleaseTime());
            System.out.println("second = " + duration.getSeconds());
            releaseTime = existUser.getReleaseTime().plusHours(penaltyTime);
            penaltyTimeToSecond = duration.getSeconds() + (penaltyTime * 60 * 60);
            penaltyUser = new RedisPenaltyUser(intraId, existUser.getPenaltyTime() + penaltyTime
                    , releaseTime, reason, existUser.getStartTime());
        } else {
            releaseTime = now.plusHours(penaltyTime);
            penaltyTimeToSecond = penaltyTime * 60 * 60;
            penaltyUser = new RedisPenaltyUser(intraId, penaltyTime, releaseTime, reason, now);
        }
        redisPenaltyUserRepository.addPenaltyUser(penaltyUser, penaltyTimeToSecond);
    }

    public RedisPenaltyUser getOnePenaltyUser(String intraId) {
        return redisPenaltyUserRepository.getOneUser(intraId);
    }

    public List<PenaltyUserResponseDto> getAllPenaltyUser() {
        List<User> allUser = userRepository.findAll();
        List<RedisPenaltyUser> penaltyUsers = redisPenaltyUserRepository.getAllPenaltyUser(allUser);
        List<PenaltyUserResponseDto> penaltyUserResponseDtos = penaltyUsers.stream()
                            .map(PenaltyUserResponseDto::new).collect(Collectors.toList());
        return penaltyUserResponseDtos;
    }

    public void releasePenaltyUser(String intraId) {
        redisPenaltyUserRepository.deletePenaltyUser(intraId);
    }

    public List<PenaltyUserResponseDto> searchPenaltyUser(String keyword) {
        List<User> users = userRepository.findByIntraIdContains(keyword);
        List<RedisPenaltyUser> allPenaltyUser = redisPenaltyUserRepository.getAllPenaltyUser(users);
        return allPenaltyUser.stream().map(PenaltyUserResponseDto::new).collect(Collectors.toList());
    }
}
