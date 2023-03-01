package io.pp.arcade.v1.admin.rank.service;

import io.jsonwebtoken.lang.Collections;
import io.pp.arcade.v1.admin.dto.create.RankCreateRequestDto;
import io.pp.arcade.v1.admin.dto.delete.RankDeleteDto;
import io.pp.arcade.v1.admin.dto.update.RankUpdateRequestDto;
import io.pp.arcade.v1.admin.users.repository.UserAdminRepository;
import io.pp.arcade.v1.domain.rank.RankRepository;
import io.pp.arcade.v1.domain.rank.dto.*;
import io.pp.arcade.v1.domain.rank.entity.Rank;
import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.GameType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankAdminService {
    private final RankRepository rankRepository;
    private final UserAdminRepository userAdminRepository;

    @Transactional
    public void addAllUserRankByNewSeason(SeasonDto seasonDto, Integer startPpp) {
        if (LocalDateTime.now().isAfter(seasonDto.getStartTime()))
            throw new BusinessException("E0001");
        List<User> users = userAdminRepository.findAll();

        List<Rank> ranks = new ArrayList<>();
        users.forEach(user -> {
            Rank userRank = Rank.from(user, GameType.SINGLE, seasonDto.getId(), startPpp);
            ranks.add(userRank);
        });
        rankRepository.saveAll(ranks);
    }

    @Transactional
    public void deleteAllUserRankBySeason(SeasonDto seasonDto) {
        if (LocalDateTime.now().isAfter(seasonDto.getStartTime()))
            throw new BusinessException("E0001");
        rankRepository.deleteAllBySeasonId(seasonDto.getId());
    }

    @Transactional
    public List<RankDto> findAll() {
        List<Rank> ranks = rankRepository.findAll();
        List<RankDto> rankDtos = ranks.stream().map(RankDto::from).collect(Collectors.toList());
        return rankDtos;
    }
}
