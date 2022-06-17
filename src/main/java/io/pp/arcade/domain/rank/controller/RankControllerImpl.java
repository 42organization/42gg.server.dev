package io.pp.arcade.domain.rank.controller;

import io.pp.arcade.domain.rank.RankService;
import io.pp.arcade.domain.rank.dto.*;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserFindDto;
import io.pp.arcade.global.util.RacketType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class RankControllerImpl implements RankController {
    private final RankService rankService;
    private final UserService userService;

    @Override
    @GetMapping(value = "/ranks/{type}")
    public RankListResponseDto rankList(Pageable pageable, String type, Integer userId) {
        // 아무런 값이 없을 떄 테스트 해보기
        RankFindListDto rankListDto = rankService.findRankList(pageable);

        /*
        List<RankFindTestDto> findListDto = findPageListDto.getRankList();
        for (RankFindTestDto findDto : findListDto){
            double winRate = (findDto.getWins() + findDto.getLosses()) / findDto.getWins();
            RankInfoDto infoDto = RankInfoDto.builder()
                    .rank(findDto.getRanking())
                    .ppp(findDto.getPpp())
                    .userId(findDto.getUser().getId())
                    .statusMessage(findDto.getUser().getStatusMessage())
                    .winRate(winRate)
                    .build();
            rankListDto.add(infoDto);
        }
        */
        UserDto user = userService.findById(UserFindDto.builder().userId(userId).build());
        RankUserDto userRankingDto = rankService.findRankById(RankFindDto.builder().intraId(user.getIntraId()).gameType(type).build());

        /*
        List<RankInfoDto> infoDtos = new ArrayList<>();
        for (RankUserDto userInfo : rankListDto.getRankList()){
             infoDtos.add(RankInfoDto.builder()
                    .intraId(userInfo.getIntraId())
                    .rank(userInfo.getRanking())
                    .ppp(userInfo.getPpp())
                    .statusMessage(userInfo.getStatusMessage())
                    .winRate(userInfo.getWinRate())
                    .build());
        }
        RankListResponseDto rankListResponseDto = RankListResponseDto.builder()
                .myRank(userRankingDto.getRanking())
                .currentPage(rankListDto.getCurrentPage())
                .totalPage(rankListDto.getTotalPage())
                .rankList(infoDtos)
                .build();

         */
        return null;
    }
}
