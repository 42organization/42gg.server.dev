package io.pp.arcade.v1.domain.rank.controller;

import io.jsonwebtoken.lang.Collections;

import io.pp.arcade.v1.domain.rank.dto.*;
import io.pp.arcade.v1.domain.rank.service.RankRedisService;
import io.pp.arcade.v1.domain.rank.service.RankService;
import io.pp.arcade.v1.domain.rank.type.RankType;
import io.pp.arcade.v1.domain.season.SeasonService;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(value = "/ranks/backup")
    public void backup() {
        List<RankUserDto> rankRedisDtos = rankRedisService.findCurrentRankList();
        SeasonDto season = seasonService.findCurrentSeason();
        RankSaveAllDto rankSaveAllDto = RankSaveAllDto.builder().rankUserDtos(rankRedisDtos).seasonDto(season).build();
        rankService.redisToMySql(rankSaveAllDto);
    }

    @Override
    @GetMapping(value = "/ranks/{gametype}")
    public RankListResponseDto rankList(Pageable pageable, GameType gametype, RankListRequestDto requestDto, HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        SeasonDto seasonDto;
        if (requestDto.getSeasonId() != null)
            seasonDto = seasonService.findSeasonById(requestDto.getSeasonId());
        else
            seasonDto = seasonService.findCurrentSeason();

        RankRankingFindDto findRankingDto = RankRankingFindDto.builder().userId(user.getId()).seasonDto(seasonDto).build();
        Integer myRanking = rankRedisService.findRankingById(findRankingDto);

        RankFindListDto rankFindListDto = RankFindListDto.builder().pageable(pageable).count(requestDto.getCount()).gameType(gametype).seasonId(seasonDto.getId()).build();
        RankListDto rankListDto = rankRedisService.findRankList(rankFindListDto);

        RankListResponseDto rankListResponseDto = RankListResponseDto.builder()
                .myRanking(myRanking)
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
    /**
     * 서버 부팅 시,
     * Redis에 랭크 데이터가 없을 경우 DB에서 가져와 Redis에 저장
     */
    @PostConstruct
    private void init (){
        if (rankRedisService.isEmpty()) {
            List<RankDto> rankList = rankService.findAll();
            if (!Collections.isEmpty(rankList)) {
                rankRedisService.mysqlToRedis(rankList);
            }
        }
    }
}
