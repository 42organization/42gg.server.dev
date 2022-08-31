package io.pp.arcade.domain.game.v1;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.game.Manager.GameManager;
import io.pp.arcade.domain.game.Manager.GameResponseManager;
import io.pp.arcade.domain.game.controller.GameControllerImpl;
import io.pp.arcade.domain.game.dto.*;
import io.pp.arcade.domain.pchange.PChangeService;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.domain.pchange.dto.PChangePageDto;
import io.pp.arcade.domain.season.SeasonService;
import io.pp.arcade.domain.season.dto.SeasonDto;
import io.pp.arcade.domain.security.jwt.TokenService;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.slotteamuser.SlotTeamUserService;
import io.pp.arcade.domain.slotteamuser.dto.SlotTeamUserDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserFindDto;
import io.pp.arcade.global.exception.BusinessException;
import io.pp.arcade.global.type.Mode;
import io.pp.arcade.global.type.StatusType;
import io.pp.arcade.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/pingpong")
public class GameControllerImplV1 {
    private final TokenService tokenService;
    private final GameService gameService;
    private final GameResponseManager gameResponseManager;
    private final SlotTeamUserService slotTeamUserService;
    private final UserService userService;
    private final PChangeService pChangeService;
    private final GameManager gameManager;
    private final CurrentMatchService currentMatchService;

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

    @GetMapping(value = "/games/normal")
    public GameResultResponseDto gameNormalResult(GameResultPageRequestDto requestDto, HttpServletRequest request) {
        tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));

        GameFindDto gameFindDto = getGameFindDto(requestDto, Mode.NORMAL);
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

    @GetMapping(value = "/games/rank")
    public GameResultResponseDto gameRankResult(GameResultPageRequestDto requestDto, HttpServletRequest request) {
        tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));

        GameFindDto gameFindDto = getGameFindDto(requestDto, Mode.RANK);
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

    @GetMapping(value = "/users/{intraId}/games/normal")
    public GameResultResponseDto gameNormalResultByUserIdAndByGameIdAndCount(@PathVariable String intraId, GameResultUserPageRequestDto requestDto, HttpServletRequest request) {
        //Pageable
        /*
         * 1. PChange에서 유저별 게임 목록을 가져온다
         * 2. 얘네를 바탕으로 게임을 다 긁어온다.
         * 3. 얘네를 DTO로 만들어준다
         */
        tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        Pageable pageable = PageRequest.of(0, requestDto.getCount());
        PChangeFindDto findDto = PChangeFindDto.builder()
                .userId(intraId)
                .gameId(requestDto.getGameId())
                .mode(Mode.NORMAL)
                .pageable(pageable)
                .build();
        PChangePageDto pChangePageDto = pChangeService.findPChangeByUserIdAfterGameIdAndGameMode(findDto);
        List<PChangeDto> pChangeLists = pChangePageDto.getPChangeList();
        List<GameDto> gameLists = pChangeLists.stream().map(PChangeDto::getGame).collect(Collectors.toList());;
        List<GameResultDto> gameResultList = new ArrayList<>();
        Integer lastGameId;
        if (gameLists.size() == 0) {
            lastGameId = 0;
        } else {
            lastGameId = gameLists.get(gameLists.size() - 1).getId();
        }
        UserDto user = userService.findByIntraId(UserFindDto.builder().intraId(intraId).build());

        gameResponseManager.putResultInGames(gameResultList, gameLists, user);

        GameResultResponseDto gameResultResponse = GameResultResponseDto.builder()
                .games(gameResultList)
                .lastGameId(lastGameId)
                .totalPage(pChangePageDto.getTotalPage())
                .currentPage(pChangePageDto.getCurrentPage() + 1)
                .build();
        return gameResultResponse;
    }

    @GetMapping(value = "/users/{intraId}/games/rank")
    public GameResultResponseDto gameRankResultByUserIdAndByGameIdAndCount(@PathVariable String intraId, GameResultUserPageRequestDto requestDto, HttpServletRequest request) {
        //Pageable
        /*
         * 1. PChange에서 유저별 게임 목록을 가져온다
         * 2. 얘네를 바탕으로 게임을 다 긁어온다.
         * 3. 얘네를 DTO로 만들어준다
         */
        tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        Pageable pageable = PageRequest.of(0, requestDto.getCount());
        PChangeFindDto findDto = PChangeFindDto.builder()
                .userId(intraId)
                .gameId(requestDto.getGameId())
                .mode(Mode.RANK)
                .pageable(pageable)
                .build();
        PChangePageDto pChangePageDto = pChangeService.findPChangeByUserIdAfterGameIdAndGameMode(findDto);
        List<PChangeDto> pChangeLists = pChangePageDto.getPChangeList();
        List<GameDto> gameLists = pChangeLists.stream().map(PChangeDto::getGame).collect(Collectors.toList());;
        List<GameResultDto> gameResultList = new ArrayList<>();
        Integer lastGameId;
        if (gameLists.size() == 0) {
            lastGameId = 0;
        } else {
            lastGameId = gameLists.get(gameLists.size() - 1).getId();
        }
        UserDto user = userService.findByIntraId(UserFindDto.builder().intraId(intraId).build());

        gameResponseManager.putResultInGames(gameResultList, gameLists, user);

        GameResultResponseDto gameResultResponse = GameResultResponseDto.builder()
                .games(gameResultList)
                .lastGameId(lastGameId)
                .totalPage(pChangePageDto.getTotalPage())
                .currentPage(pChangePageDto.getCurrentPage() + 1)
                .build();
        return gameResultResponse;
    }


    @PostMapping(value = "/games/result/normal")
    public void gameNormalSave(HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));

        CurrentMatchDto currentMatch = gameManager.checkIfCurrentMatchExist(user);
        GameDto game = currentMatch.getGame();
        if (game == null) {
            throw new BusinessException("E0001");
        }
        List<SlotTeamUserDto> slotTeamUsers = slotTeamUserService.findAllBySlotId(currentMatch.getSlot().getId());
        gameManager.removeCurrentMatch(game);

        gameService.modifyGameStatus(GameModifyStatusDto.builder().gameId(game.getId()).status(StatusType.END).build());

        throw new ResponseStatusException(HttpStatus.ACCEPTED, "");
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
