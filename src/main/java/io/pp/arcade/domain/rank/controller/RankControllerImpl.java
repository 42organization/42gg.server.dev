package io.pp.arcade.domain.rank.controller;

import io.pp.arcade.domain.rank.RankService;
import io.pp.arcade.domain.rank.dto.*;
import io.pp.arcade.domain.security.jwt.TokenService;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserFindDto;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.util.HeaderUtil;
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

    @Override
    @GetMapping(value = "/ranks/{gameType}")
    public RankListResponseDto rankList(Pageable pageable, GameType gameType, HttpServletRequest request) {
        // 아무런 값이 없을 떄 테스트 해보기
        RankFindListDto rankListDto = rankService.findRankList(pageable, gameType);
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        RankUserDto userRank = rankService.findRankById(RankFindDto.builder().intraId(user.getIntraId()).gameType(gameType).build());

        RankListResponseDto rankListResponseDto = RankListResponseDto.builder()
                .myRank(userRank.getRank())
                .currentPage(rankListDto.getCurrentPage())
                .totalPage(rankListDto.getTotalPage())
                .rankList(rankListDto.getRankList())
                .build();
        return rankListResponseDto;
    }
}
