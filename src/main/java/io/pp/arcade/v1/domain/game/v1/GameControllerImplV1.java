package io.pp.arcade.v1.domain.game.v1;

import io.pp.arcade.v1.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.v1.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.v1.domain.currentmatch.dto.CurrentMatchRemoveDto;
import io.pp.arcade.v1.domain.game.GameService;
import io.pp.arcade.v1.domain.game.Manager.GameManager;
import io.pp.arcade.v1.domain.game.Manager.GameResponseManager;

import io.pp.arcade.v1.domain.game.dto.*;
import io.pp.arcade.v1.domain.pchange.PChangeService;
import io.pp.arcade.v1.domain.pchange.dto.GameExpAndPppResultDto;
import io.pp.arcade.v1.domain.pchange.dto.PChangeDto;
import io.pp.arcade.v1.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.v1.domain.season.SeasonService;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.domain.user.UserService;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.StatusType;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/pingpong")
public class GameControllerImplV1 {
    private final TokenService tokenService;
    private final GameService gameService;
    private final GameResponseManager gameResponseManager;
    private final UserService userService;
    private final PChangeService pChangeService;
    private final GameManager gameManager;
    private final CurrentMatchService currentMatchService;

    private final SeasonService seasonService;

    @GetMapping(value = "/games")
    public GameResultResponseDto gameResult(@ModelAttribute @Valid GameResultPageRequestDto requestDto, HttpServletRequest request) {
        tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));

        GameFindDto gameFindDto = getGameFindDto(requestDto, requestDto.getMode());
        GameResultListDto resultPageDto = gameService.v1_findGamesAfterId(gameFindDto);

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
                .isLast(resultPageDto.getIsLast())
                .build();
        return gameResultResponse;
    }

    @GetMapping(value = "/games/normal")
    public GameResultResponseDto gameNormalResult(@ModelAttribute @Valid GameResultPageRequestDto requestDto, HttpServletRequest request) {
        tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));

        GameFindDto gameFindDto = getGameFindDto(requestDto, Mode.NORMAL);
        GameResultListDto resultPageDto = gameService.v1_findGamesAfterId(gameFindDto);

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
                .isLast(resultPageDto.getIsLast())
                .build();
        return gameResultResponse;
    }

    @GetMapping(value = "/games/rank")
    public GameResultResponseDto gameRankResult(@ModelAttribute @Valid GameResultPageRequestDto requestDto, HttpServletRequest request) {
        tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));

        GameFindDto gameFindDto = getGameFindDto(requestDto, Mode.RANK);
        GameResultListDto resultPageDto = gameService.v1_findGamesAfterId(gameFindDto);

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
                .isLast(resultPageDto.getIsLast())
                .build();
        return gameResultResponse;
    }

    @PostMapping(value = "/games/result/normal")
    public synchronized void gameNormalSave(HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));

        CurrentMatchDto currentMatch = gameManager.checkIfCurrentMatchExist(user);
        GameDto game = currentMatch.getGame();
        if (game == null || game.getMode() == Mode.RANK) {
            throw new BusinessException("E0001");
        } else if (game.getStatus() == StatusType.END) {
            throw new ResponseStatusException(HttpStatus.CREATED, "");
        }

        gameManager.modifyUserExp(game);
        gameService.modifyGameStatus(GameModifyStatusDto.builder().game(game).status(StatusType.END).build());

        throw new ResponseStatusException(HttpStatus.CREATED, "");
    }

    @GetMapping(value = "/games/{gameId}/result/normal")
    public GameExpResultResponseDto gameExpResult(@PathVariable Integer gameId, HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        GameDto game = gameService.findById(gameId);

        GameExpAndPppResultDto gameExpResult = pChangeService.findPChangeExpByUserAndGame(PChangeFindDto.builder().user(user).game(game).build());
        GameExpResultResponseDto responseDto = GameExpResultResponseDto.builder()
                .beforeMaxExp(gameExpResult.getBeforeMaxExp())
                .beforeExp(gameExpResult.getBeforeExp())
                .beforeLevel(gameExpResult.getBeforeLevel())
                .afterMaxExp(gameExpResult.getAfterMaxExp())
                .increasedExp(gameExpResult.getIncreasedExp())
                .increasedLevel(gameExpResult.getIncreasedLevel())
                .build();
        return responseDto;
    }

    @GetMapping(value = "/games/{gameId}/result/rank")
    public GameExpAndPppResultResponseDto gameExpAndPppResult(@PathVariable Integer gameId, HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        GameDto game = gameService.findById(gameId);

        GameExpAndPppResultDto gameResult = pChangeService.findPChangeExpByUserAndGame(PChangeFindDto.builder().user(user).game(game).build());

        GameExpAndPppResultResponseDto responseDto = (GameExpAndPppResultResponseDto.builder()
                .afterMaxExp(gameResult.getAfterMaxExp())
                .beforeMaxExp(gameResult.getBeforeMaxExp())
                .beforeExp(gameResult.getBeforeExp())
                .beforeLevel(gameResult.getBeforeLevel())
                .increasedExp(gameResult.getIncreasedExp())
                .increasedLevel(gameResult.getIncreasedLevel())
                .changedPpp(gameResult.getPppChange())
                .beforePpp(gameResult.getPppResult() - gameResult.getPppChange())
                .build());

        return responseDto;
    }

    private GameFindDto getGameFindDto(GameResultPageRequestDto requestDto, Mode mode) {
        /* 시즌 조회 */
        Integer seasonId = requestDto.getSeason();
        if (seasonId != null && seasonId == 0) {
            SeasonDto currentSeason = seasonService.findLatestRankSeason();
            if (currentSeason != null) {
                seasonId = seasonService.findLatestRankSeason().getId();
            }
        }
        return GameFindDto.builder()
                .id(requestDto.getGameId())
                .status(requestDto.getStatus())
                .mode(mode)
                .seasonId(seasonId)
                .count(requestDto.getCount()).build();
    }
}
