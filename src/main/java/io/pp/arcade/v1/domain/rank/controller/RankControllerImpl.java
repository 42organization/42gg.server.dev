package io.pp.arcade.v1.domain.rank.controller;

import io.pp.arcade.v1.domain.rank.dto.*;
import io.pp.arcade.v1.domain.rank.service.RankService;
import io.pp.arcade.v1.domain.season.SeasonService;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/pingpong")
public class RankControllerImpl implements RankController {
    private final RankService rankService;
    private final TokenService tokenService;
    private final SeasonService seasonService;

    @Override
    @GetMapping(value = "/ranks/{gametype}")
    public RankListResponseDto rankList(Pageable pageable, GameType gametype, RankListRequestDto requestDto, HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        Integer seasonId = requestDto.getSeason();
        SeasonDto seasonDto;
        if (seasonId != null && seasonId > 0)
            seasonDto = seasonService.findSeasonById(requestDto.getSeason());
        else
            seasonDto = seasonService.findLatestRankSeason();

        RankRankingFindDto findRankingDto = RankRankingFindDto.builder().userId(user.getId()).seasonDto(seasonDto).build();
        Integer myRanking = rankService.findRankingById(findRankingDto);

        RankFindListDto rankFindListDto = RankFindListDto.builder().pageable(pageable).count(requestDto.getCount()).gameType(gametype).seasonId(seasonDto.getId()).build();
        RankListDto rankListDto = rankService.findRankList(rankFindListDto);

        RankListResponseDto rankListResponseDto = RankListResponseDto.builder()
                .myRank(myRanking)
                .currentPage(rankListDto.getCurrentPage())
                .totalPage(rankListDto.getTotalPage())
                .rankList(rankListDto.getRankList())
                .build();
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
}
