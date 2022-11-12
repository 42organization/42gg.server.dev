package io.pp.arcade.v1.domain.rank.service;

import io.jsonwebtoken.lang.Collections;
import io.pp.arcade.v1.domain.admin.dto.create.RankCreateRequestDto;
import io.pp.arcade.v1.domain.admin.dto.delete.RankDeleteDto;
import io.pp.arcade.v1.domain.admin.dto.update.RankUpdateRequestDto;
import io.pp.arcade.v1.domain.rank.entity.Rank;
import io.pp.arcade.v1.domain.rank.RankRepository;

import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.domain.rank.dto.*;
import io.pp.arcade.v1.global.type.GameType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankService {
    private final RankRepository rankRepository;
    private final UserRepository userRepository;
    @Transactional
    public RankListDto  findAllBySeasonId(Integer seasonId, Pageable pageable) {
        Page<Rank> pageRank = rankRepository.findAllBySeasonId(seasonId, pageable);
        List<RankUserDto> rankUserDtos = pageRank.stream().map(RankUserDto::from).collect(Collectors.toList());
        RankListDto rankListDto =  RankListDto.builder()
                .rankList(rankUserDtos)
                .currentPage(pageRank.getNumber())
                .totalPage(pageRank.getTotalPages())
                .build();
        return rankListDto;
    }
    @Transactional
    public RankDto findBySeasonIdAndUserId(Integer seasonId, Integer userId) {
        Rank rank = rankRepository.findBySeasonIdAndUserId(seasonId, userId).orElse(null);
        if (rank == null) {
            return null;
        }
        return RankDto.from(rank);
    }
    
    @Transactional
    public VipListResponseDto vipList(UserDto curUser, Integer count, Pageable pageable) {
        Integer pageNum = pageable.getPageNumber() < 1 ? 0 : pageable.getPageNumber() - 1;
        pageable = PageRequest.of(pageNum, count);

        Page<User> userPage = userRepository.findAllByOrderByTotalExpDesc(pageable);
        Integer myRank = curUser.getTotalExp() == 0 ? -1 : userRepository.findExpRankingByIntraId(curUser.getIntraId());

        List<VipUserDto> vipUserList = new ArrayList<>();
        Integer index = pageable.getPageSize() * pageable.getPageNumber();
        for (User user : userPage) {
            vipUserList.add(VipUserDto.from(user, ++index));
        }
        return VipListResponseDto.builder()
                .rankList(vipUserList)
                .myRank(myRank)
                .totalPage(userPage.getTotalPages())
                .currentPage(pageNum + 1)
                .build();
    }

    @Transactional
    public void redisToMySql(RankSaveAllDto saveAllDto) {
        if (saveAllDto.getSeasonDto() == null) {
            throw new BusinessException("{server.internal.error}");
        }
        /* 현재 시즌의 모든 랭크리스트 구하기 */
        SeasonDto seasonDto = saveAllDto.getSeasonDto();
        List<Rank> ranks =  rankRepository.findAllBySeasonId(seasonDto.getId());

        HashMap<String, Rank> rankHashMap = new HashMap<>();
        ranks.forEach(rank -> rankHashMap.put(rank.getUser().getIntraId(), rank));

        List<Rank> rankList = new ArrayList<>();
        List<RankUserDto> rankRedisDtos = saveAllDto.getRankUserDtos();
        rankRedisDtos.forEach(rankUser -> {
            Rank rank = rankHashMap.get(rankUser.getIntraId());
            if (rank == null) {
                User user = userRepository.getUserByIntraId(rankUser.getIntraId());
                rank = Rank.builder()
                        .ranking(rankUser.getRank())
                        .losses(rankUser.getLosses())
                        .wins(rankUser.getWins())
                        .ppp(rankUser.getPpp())
                        .user(user)
                        .racketType(user.getRacketType())
                        .statusMessage(user.getStatusMessage())
                        .seasonId(seasonDto.getId())
                        .gameType(GameType.SINGLE)
                        .build();
            } else {
                rank.updateRedisInfo(rankUser.getPpp(),rankUser.getWins(), rankUser.getLosses(), rankUser.getRank());
            }
            rankList.add(rank);
        });
        if (!Collections.isEmpty(rankList))
            rankRepository.saveAll(rankList);
    }

    @Transactional
    public List<RankDto> findAll() {
        List<Rank> ranks = rankRepository.findAll();
        List<RankDto> rankDtos = ranks.stream().map(RankDto::from).collect(Collectors.toList());
        return rankDtos;
    }

    @Transactional
    public void createRankByAdmin(RankCreateRequestDto createRequestDto) {
        User user = userRepository.findById(createRequestDto.getUserId()).orElseThrow();
        Rank rank = Rank.builder()
                .user(user)
                .seasonId(createRequestDto.getSeasonId())
                .racketType(createRequestDto.getRacketType())
                .ppp(createRequestDto.getPpp())
                .ranking(createRequestDto.getRangking())
                .wins(createRequestDto.getWins())
                .losses(createRequestDto.getLosses())
                .build();
        rankRepository.save(rank);
    }

    @Transactional
    public void updateRankByAdmin(RankUpdateRequestDto updateRequestDto) {
        Rank rank = rankRepository.findById(updateRequestDto.getRankId()).orElseThrow();
        rank.update(updateRequestDto.getPpp(), updateRequestDto.getWins(), updateRequestDto.getLosses());
    }

    @Transactional
    public void deleteRankByAdmin(RankDeleteDto deleteDto) {
        Rank rank = rankRepository.findById(deleteDto.getRankId()).orElseThrow();
        rankRepository.delete(rank);
    }

    @Transactional
    public List<RankDto> findRankByAdmin(Pageable pageable) {
        Page<Rank> ranks = rankRepository.findAllByOrderByIdDesc(pageable);
        List<RankDto> rankDtos = ranks.stream().map(RankDto::from).collect(Collectors.toList());
        return rankDtos;
    }

    public RankUserDto findUserBySeasonId(Integer userSeason, UserDto userDto) {
        Rank rank = rankRepository.findBySeasonIdAndUserId(userSeason, userDto.getId()).orElse(null);
        RankUserDto rankUserDto = null;
        if (rank != null)
            rankUserDto = RankUserDto.from(rank);
        return rankUserDto;
    }
}
