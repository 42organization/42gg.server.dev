package io.pp.arcade.domain.game.v1;

import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.game.Manager.GameResponseManager;
import io.pp.arcade.domain.game.dto.*;
import io.pp.arcade.domain.season.SeasonService;
import io.pp.arcade.domain.season.dto.SeasonDto;
import io.pp.arcade.domain.security.jwt.TokenService;
import io.pp.arcade.global.type.Mode;
import io.pp.arcade.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/pingpong")
public class GameControllerImplV1 {
    private final TokenService tokenService;
    private final GameService gameService;
    private final GameResponseManager gameResponseManager;

    private final SeasonService seasonService;

    @GetMapping(value = "/games")
    public GameResultResponseDto gameResult(GameResultPageRequestDto requestDto, HttpServletRequest request) {
        tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));

        GameFindDto gameFindDto = getGameFindDto(requestDto, requestDto.getMode());
        GameResultPageDto resultPageDto = gameService.v1_findGamesAfterId(gameFindDto);

        List<GameResultDto> gameResultList = new ArrayList<>();
        List<GameDto> gameLists = resultPageDto.getGameList();
        gameResponseManager.putResultInGames(gameResultList, gameLists, null);

        Integer lastGameId;
        if (gameLists.size() == 0) {
            lastGameId = 0;
        } else {
            lastGameId = gameLists.get(gameLists.size() - 1).getId();
        }

        GameResultResponseDto gameResultResponse = GameResultResponseDto.builder()
                .games(gameResultList)
                .lastGameId(lastGameId)
                .totalPage(resultPageDto.getTotalPage())
                .currentPage(resultPageDto.getCurrentPage() + 1)
                .build();
        return gameResultResponse;
    }

    private GameFindDto getGameFindDto(GameResultPageRequestDto requestDto, Mode mode) {
        /* 시즌 조회 */
        Integer seasonId = null;
        if (requestDto.getSeason() == null) {
            SeasonDto currentSeason = seasonService.findCurrentSeason();
            if (currentSeason != null) {
                seasonId = seasonService.findCurrentSeason().getId();
            }
        }
        Pageable pageable = PageRequest.of(0, requestDto.getCount());
        return GameFindDto.builder().id(requestDto.getGameId()).status(requestDto.getStatus()).mode(mode).seasonId(seasonId).pageable(pageable).build();
    }
}
