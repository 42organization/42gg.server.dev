package io.pp.arcade.v1.admin.penalty.service;

import io.pp.arcade.v1.admin.penalty.RedisPenaltyUser;
import io.pp.arcade.v1.admin.penalty.dto.PenaltyListResponseDto;
import io.pp.arcade.v1.admin.penalty.dto.PenaltyUserResponseDto;
import io.pp.arcade.v1.admin.penalty.repository.RedisPenaltyUserRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.redis.Key;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public PenaltyListResponseDto getAllPenaltyUser(int page, int size) {
        List<User> allUser = userRepository.findAll();
        List<RedisPenaltyUser> penaltyUsers = redisPenaltyUserRepository.getAllPenaltyUser(allUser);
        List<PenaltyUserResponseDto> penaltyUserResponseDtos = penaltyUsers.stream()
                            .map(PenaltyUserResponseDto::new).collect(Collectors.toList());
        int totalPage = (penaltyUserResponseDtos.size() % size == 0) ?
                penaltyUserResponseDtos.size() / size : (penaltyUserResponseDtos.size() / size + 1);
        List<PenaltyUserResponseDto> pagedUserDtos = paging(size, page, penaltyUserResponseDtos);
        return new PenaltyListResponseDto(pagedUserDtos, page + 1, totalPage);
    }

    public void releasePenaltyUser(String intraId) {
        redisPenaltyUserRepository.deletePenaltyUser(intraId);
    }

    public PenaltyListResponseDto searchPenaltyUser(String keyword, int page, int size) {
        List<User> users = userRepository.findByIntraIdContains(keyword);
        List<RedisPenaltyUser> allPenaltyUser = redisPenaltyUserRepository.getAllPenaltyUser(users);
        List<PenaltyUserResponseDto> penaltyUserResponseDtos =
                allPenaltyUser.stream().map(PenaltyUserResponseDto::new).collect(Collectors.toList());
        int totalPage = (penaltyUserResponseDtos.size() % size == 0) ?
                penaltyUserResponseDtos.size() / size : (penaltyUserResponseDtos.size() / size + 1);
        List<PenaltyUserResponseDto> pagedUserDtos = paging(size, page, penaltyUserResponseDtos);
        return new PenaltyListResponseDto(pagedUserDtos, page + 1, totalPage);
    }

    private static List<PenaltyUserResponseDto> paging(int size,  int page,
                                                       List<PenaltyUserResponseDto> penaltyUserResponseDtos) {
        List<List<PenaltyUserResponseDto>> tmpList = new ArrayList<>();
        for (int i = 0; i < page + 1; i++) {
            tmpList.add(new ArrayList<>());
            for (int j = 0; j < size; j++){
                if (i * size + j < penaltyUserResponseDtos.size())
                    tmpList.get(i).add(penaltyUserResponseDtos.get(i * size + j));
            }
        }
        return tmpList.get(page);
    }
}
