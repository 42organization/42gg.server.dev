package io.pp.arcade.v1.domain.rank.controller;

import io.jsonwebtoken.lang.Collections;

import io.pp.arcade.v1.domain.rank.dto.*;
import io.pp.arcade.v1.domain.rank.service.RankRedisService;
import io.pp.arcade.v1.domain.rank.service.RankService;
import io.pp.arcade.v1.domain.rank.type.RankType;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.SeasonService;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/pingpong")
public class RankControllerImpl implements RankController {
    private final RankService rankService;
    private final RankRedisService rankRedisService;
    private final TokenService tokenService;
    private final SeasonService seasonService;

    @Override
    @GetMapping(value = "/ranks/{gametype}")
    public RankListResponseDto rankList(Pageable pageable, GameType gametype, RankListRequestDto requestDto, HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        SeasonDto curSeason = seasonService.findCurrentSeason();
        RankListResponseDto rankListResponseDto = getRankList(requestDto, user, gametype, pageable, curSeason);
        return rankListResponseDto;
    }

    @Override
    @GetMapping(value = "/vip")
    public VipListResponseDto vipList(Pageable pageable, Integer count, HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        VipListResponseDto vipListResponseDto = rankService.vipList(user, count, pageable);
        //vipListResponseDto.setMyRank(123);
        return vipListResponseDto;
    }

    private RankListResponseDto getRankList(RankListRequestDto requestDto, UserDto userDto, GameType gameType, Pageable pageable, SeasonDto curSeason){
        RankUserDto userRank = null;
        RankListDto rankListDto = null;
        Integer count = (requestDto.getCount() == null) ? pageable.getPageSize() : requestDto.getCount();
        Integer pageNum = pageable.getPageNumber() < 1 ? 0 : pageable.getPageNumber() - 1;
        Integer userSeason = requestDto.getSeason();

        Boolean isCurSeason = (userSeason == null) || (curSeason.getId().equals(userSeason));
        if (isCurSeason) {
            rankListDto = rankRedisService.findRankList(RankFindListDto.builder().pageable(pageable).gameType(gameType).count(count).build());
            userRank = rankRedisService.findRankById(RankFindDto.builder().intraId(userDto.getIntraId()).gameType(gameType).build());
        }else {
            rankListDto = rankService.findAllBySeasonId(userSeason, PageRequest.of(pageNum, count));
            userRank = rankService.findUserBySeasonId(userSeason, userDto);
        }

        /* 선택 시즌에 유저가 없을 경우 NULL 반환*/
        Integer userRanking = (userRank != null) ? userRank.getRank() : RankType.UNRANK;
        RankListResponseDto rankListResponseDto = RankListResponseDto.builder()
                .myRank(userRanking)
                .currentPage(pageNum + 1)
                .totalPage(rankListDto.getTotalPage())
                .rankList(rankListDto.getRankList())
                .build();
        return rankListResponseDto;
    }

    @PostConstruct
    private void init (){
        if (rankRedisService.isEmpty()) {
            List<RankDto> rankList = rankService.findAll();
            if (!Collections.isEmpty(rankList)) {
                rankRedisService.saveAll(rankList);
            }
        }
    }
}
