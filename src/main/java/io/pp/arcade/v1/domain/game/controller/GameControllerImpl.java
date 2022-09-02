package io.pp.arcade.v1.domain.game.controller;

import io.pp.arcade.v1.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.v1.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.v1.domain.event.EventService;
import io.pp.arcade.v1.domain.event.dto.EventUserDto;
import io.pp.arcade.v1.domain.event.dto.FindEventDto;
import io.pp.arcade.v1.domain.event.dto.SaveEventUserDto;
import io.pp.arcade.v1.domain.game.GameService;
import io.pp.arcade.v1.domain.game.Manager.GameManager;
import io.pp.arcade.v1.domain.game.Manager.GameResponseManager;
import io.pp.arcade.v1.domain.game.dto.*;
import io.pp.arcade.v1.domain.noti.NotiService;
import io.pp.arcade.v1.domain.noti.dto.NotiAddDto;
import io.pp.arcade.v1.domain.pchange.PChangeService;
import io.pp.arcade.v1.domain.pchange.dto.PChangeDto;
import io.pp.arcade.v1.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.v1.domain.pchange.dto.PChangePageDto;
import io.pp.arcade.v1.domain.rank.service.RankRedisService;
import io.pp.arcade.v1.domain.season.SeasonService;
import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUserService;
import io.pp.arcade.v1.domain.slotteamuser.dto.SlotTeamUserDto;
import io.pp.arcade.v1.domain.team.TeamService;
import io.pp.arcade.v1.domain.team.dto.TeamsUserListDto;
import io.pp.arcade.v1.domain.user.UserService;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.domain.user.dto.UserFindDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.NotiType;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class GameControllerImpl implements GameController {
    private final GameService gameService;
    private final TeamService teamService;
    private final SlotTeamUserService slotTeamUserService;
    private final UserService userService;
    private final PChangeService pChangeService;
    private final CurrentMatchService currentMatchService;
    private final RankRedisService rankRedisService;
    private final TokenService tokenService;
    private final SeasonService seasonService;
    private final EventService eventService;
    private final NotiService notiService;
    private final GameResponseManager gameResponseManager;
    private final GameManager gameManager;

    @Override
    @GetMapping(value = "/games/result")
    public GameUserInfoResponseDto gameUserInfo(HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        CurrentMatchDto currentMatch = currentMatchService.findCurrentMatchByUser(user);

        gameManager.checkIfUserHavePlayingGame(currentMatch);

        SlotDto slot = currentMatch.getSlot();

        //figuring out team number for myteam and enemyteam, and put each team's user infos in the right team

        //make Dto to return
        TeamsUserListDto matchUsers = teamService.findUserListInTeams(slot, user);
        GameUserInfoResponseDto gameUserInfoResponseDto = GameUserInfoResponseDto.builder()
                .matchTeamsInfo(MatchTeamsInfoDto.builder().myTeam(matchUsers.getMyTeam())
                        .enemyTeam(matchUsers.getEnemyTeam()).build())
                .mode(slot.getMode())
                .startTime(slot.getTime())
                .build();
        return gameUserInfoResponseDto;
    }

    @Override
    @PostMapping(value = "/games/result")
    public void gameResultSave(GameResultRequestDto requestDto, HttpServletRequest request) throws MessagingException {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        validateInput(requestDto);

        CurrentMatchDto currentMatch = gameManager.checkIfCurrentMatchExist(user);
        GameDto game = currentMatch.getGame();
        if (game == null) {
            throw new BusinessException("E0001");
        }
        List<SlotTeamUserDto> slotTeamUsers = slotTeamUserService.findAllBySlotId(currentMatch.getSlot().getId());
        gameManager.removeCurrentMatch(game);
        // modify team with game result
        gameManager.modifyTeams(game, requestDto, slotTeamUsers);
        slotTeamUsers = slotTeamUserService.findAllBySlotId(currentMatch.getSlot().getId());
        // figuring out team number for myteam and enemyteam
        // modify users with game result
        gameManager.modifyUserExp(user, game);
        gameManager.endGameStatus(game);
        // checkEvent(game);
        // modify users' ranks with game result
        throw new ResponseStatusException(HttpStatus.CREATED, "");
    }

    private void validateInput(GameResultRequestDto requestDto) {
        requestDto.getMyTeamScore();
        requestDto.getEnemyTeamScore();
    }

    @Override
    @GetMapping(value = "/games")
    public GameResultResponseDto gameResultByGameIdAndCount(GameResultPageRequestDto requestDto, HttpServletRequest request) {
        tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        Pageable pageable = PageRequest.of(0, requestDto.getCount());
        GameResultPageDto resultPageDto = gameService.v1_findGamesAfterId(GameFindDto.builder().id(requestDto.getGameId()).status(requestDto.getStatus()).pageable(pageable).build());
        List<GameResultDto> gameResultList = new ArrayList<>();
        List<GameDto> gameLists = resultPageDto.getGameList();
        Integer lastGameId;
        if (gameLists.size() == 0) {
            lastGameId = 0;
        } else {
            lastGameId = gameLists.get(gameLists.size() - 1).getId();
        }
        gameResponseManager.putResultInGames(gameResultList, gameLists, null);

        GameResultResponseDto gameResultResponse = GameResultResponseDto.builder()
                .games(gameResultList)
                .lastGameId(lastGameId)
                .totalPage(resultPageDto.getTotalPage())
                .currentPage(resultPageDto.getCurrentPage() + 1)
                .build();
        return gameResultResponse;
    }

    @Override
    @GetMapping(value = "/users/{intraId}/games")
    public GameResultResponseDto gameResultByUserIdAndByGameIdAndCount(String intraId, GameResultUserPageRequestDto requestDto, HttpServletRequest request) {
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
                .pageable(pageable)
                .build();
        PChangePageDto pChangePageDto = pChangeService.findPChangeByUserIdAfterGameId(findDto);
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

    private void checkEvent(GameDto game) throws MessagingException {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        if (random.nextInt() % 100 == 42){
        }
    }

    private void addEventUser(UserDto user, String eventName) throws MessagingException {
        List<EventUserDto> eventUserList = eventService.findByEventName(FindEventDto.builder().eventName("랜덤 당첨 이벤트").build());
        if (user != null && checkUserExist(user.getIntraId(), eventUserList)) {
            SaveEventUserDto saveEventUserDto = SaveEventUserDto.builder()
                    .intraId(user.getIntraId())
                    .eventName(eventName)
                    .build();
            NotiAddDto notiAddDto = NotiAddDto.builder()
                    .user(user)
                    .type(NotiType.ANNOUNCE)
                    .message(user.getIntraId() + "님이 랜덤 슬롯 이벤트에 당첨되었습니다🎉")
                    .build();
            eventService.saveEventUser(saveEventUserDto);
            notiService.createEventNotiForAll(notiAddDto);
            notiService.sendEventMail(notiAddDto);
        }
    }

    private boolean checkUserExist(String intraId, List<EventUserDto> eventUserList) {
        for (EventUserDto eventUser : eventUserList) {
            if (eventUser.getIntraId().equals(intraId)) {
                return false;
            }
        }
        return true;
    }
}
