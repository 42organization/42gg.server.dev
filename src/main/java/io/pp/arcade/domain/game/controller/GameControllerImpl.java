package io.pp.arcade.domain.game.controller;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchFindDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchRemoveDto;
import io.pp.arcade.domain.event.EventService;
import io.pp.arcade.domain.event.dto.EventUserDto;
import io.pp.arcade.domain.event.dto.FindEventDto;
import io.pp.arcade.domain.event.dto.SaveEventUserDto;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.game.dto.*;
import io.pp.arcade.domain.noti.NotiService;
import io.pp.arcade.domain.noti.dto.NotiAddDto;
import io.pp.arcade.domain.pchange.PChangeService;
import io.pp.arcade.domain.pchange.dto.PChangeAddDto;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.pchange.dto.PChangeFindDto;
import io.pp.arcade.domain.pchange.dto.PChangePageDto;
import io.pp.arcade.domain.rank.RankRedis;
import io.pp.arcade.domain.rank.service.RankRedisService;
import io.pp.arcade.domain.rank.dto.RankFindDto;
import io.pp.arcade.domain.rank.dto.RankUpdateDto;
import io.pp.arcade.domain.rank.dto.RankUserDto;
import io.pp.arcade.domain.security.jwt.TokenService;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.slotteamuser.SlotTeamUser;
import io.pp.arcade.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.domain.slotteamuser.SlotTeamUserService;
import io.pp.arcade.domain.slotteamuser.dto.SlotTeamUserDto;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamService;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.team.dto.TeamModifyGameResultDto;
import io.pp.arcade.domain.team.dto.TeamPosDto;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserFindDto;
import io.pp.arcade.domain.user.dto.UserModifyPppDto;
import io.pp.arcade.global.exception.BusinessException;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.NotiType;
import io.pp.arcade.global.type.StatusType;
import io.pp.arcade.global.util.EloRating;
import io.pp.arcade.global.util.HeaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private final EventService eventService;
    private final NotiService notiService;

    @Override
    @GetMapping(value = "/games/result")
    public GameUserInfoResponseDto gameUserInfo(HttpServletRequest request) {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        CurrentMatchDto currentMatch = currentMatchService.findCurrentMatchByUser(user);

        checkIfUserHavePlayingGame(currentMatch);

        SlotDto slot = currentMatch.getSlot();

        //figuring out team number for myteam and enemyteam, and put each team's user infos in the right team

        //make Dto to return
        TeamPosDto teamPosDto = teamService.findUsersByTeamPos(slot, user);
        GameUserInfoResponseDto gameUserInfoResponseDto = GameUserInfoResponseDto.builder()
                .myTeam(teamPosDto.getMyTeam())
                .enemyTeam(teamPosDto.getEnemyTeam())
                .build();
        return gameUserInfoResponseDto;
    }

    @Override
    @PostMapping(value = "/games/result")
    public void gameResultSave(GameResultRequestDto requestDto, HttpServletRequest request) throws MessagingException {
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        CurrentMatchDto currentMatch = currentMatchService.findCurrentMatchByUser(user);
        validateInput(requestDto);
        // if the result already exists, throw 202 error
        if (currentMatch == null) {
            throw new ResponseStatusException(HttpStatus.ACCEPTED, "");
        }
        GameDto game = currentMatch.getGame();
        List<SlotTeamUserDto> slotTeamUsers = slotTeamUserService.findAllBySlotId(currentMatch.getSlot().getId());
        if (game == null) {
            throw new BusinessException("E0001");
        }
        removeCurrentMatch(game);
        // modify team with game result
        modifyTeams(game, requestDto, slotTeamUsers);
        slotTeamUsers = slotTeamUserService.findAllBySlotId(currentMatch.getSlot().getId());
        // figuring out team number for myteam and enemyteam
        // modify users with game result
//        modifyUsersPppAndPChange(game, requestDto, slotTeamUsers);
        endGameStatus(game);
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
        GameResultPageDto resultPageDto = gameService.findGamesAfterId(GameFindDto.builder().id(requestDto.getGameId()).status(requestDto.getStatus()).pageable(pageable).build());
        List<GameResultDto> gameResultList = new ArrayList<>();
        List<GameDto> gameLists = resultPageDto.getGameList();
        Integer lastGameId;
        if (gameLists.size() == 0) {
            lastGameId = 0;
        } else {
            lastGameId = gameLists.get(gameLists.size() - 1).getId();
        }
        putResultInGames(gameResultList, gameLists, null);

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

        putResultInGames(gameResultList, gameLists, user);

        GameResultResponseDto gameResultResponse = GameResultResponseDto.builder()
                .games(gameResultList)
                .lastGameId(lastGameId)
                .totalPage(pChangePageDto.getTotalPage())
                .currentPage(pChangePageDto.getCurrentPage() + 1)
                .build();
        return gameResultResponse;
    }


    private void checkIfUserHavePlayingGame(CurrentMatchDto currentMatch) {
        if (currentMatch == null) {
            throw new BusinessException("E0001");
        }
        GameDto game = currentMatch.getGame();
        if (game == null) {
            throw new BusinessException("E0001");
        }
    }

    private void endGameStatus(GameDto game) {
        GameModifyStatusDto modifyStatusDto = GameModifyStatusDto.builder()
                .gameId(game.getId())
                .status(StatusType.END).build();
        gameService.modifyGameStatus(modifyStatusDto);
    }

    private void modifyTeams(GameDto game, GameResultRequestDto requestDto, List<SlotTeamUserDto> slotTeamUsers) {
        Integer gamePpp = game.getSlot().getGamePpp();
        SlotDto slot = game.getSlot();
        Boolean isOneSide = Math.abs(requestDto.getMyTeamScore() - requestDto.getEnemyTeamScore()) == 2;

        for(SlotTeamUserDto slotTeamUser : slotTeamUsers) {
            TeamDto team;
            TeamModifyGameResultDto teamModifyGameResultDto;
            Integer enemyPpp;
            Integer pppChange;
            Integer score;
            Boolean isWin;
            TeamPosDto teamPosDto = teamService.findUsersByTeamPos(slotTeamUser.getSlot(), slotTeamUser.getUser());

            if (teamPosDto.getMyTeam().contains(slotTeamUser.getUser().getIntraId())) {
                enemyPpp = (gamePpp * 2 - slotTeamUser.getTeam().getTeamPpp());
                team = slotTeamUser.getTeam();
                isWin = requestDto.getMyTeamScore() > requestDto.getEnemyTeamScore();
                score = requestDto.getMyTeamScore();
            } else {
                isWin = requestDto.getMyTeamScore() < requestDto.getEnemyTeamScore();
                enemyPpp = slotTeamUser.getTeam().getTeamPpp();
                team = slotTeamUser.getTeam();
                score = requestDto.getEnemyTeamScore();
            }
            pppChange = EloRating.pppChange(slotTeamUser.getUser().getPpp(), enemyPpp, isWin, isOneSide);
            teamModifyGameResultDto = TeamModifyGameResultDto.builder()
                    .teamId(team.getId())
                    .score(score)
                    .win(isWin)
                    .build();
            UserModifyPppDto modifyPppDto = UserModifyPppDto.builder()
                    .userId(slotTeamUser.getUser().getId())
                    .ppp(slotTeamUser.getUser().getPpp() + pppChange)
                    .build();
            PChangeAddDto pChangeAddDto = PChangeAddDto.builder()
                    .gameId(game.getId())
                    .userId(slotTeamUser.getUser().getId())
                    .pppChange(pppChange)
                    .pppResult(slotTeamUser.getUser().getPpp() + pppChange)
                    .build();
            RankUpdateDto rankUpdateDto =  RankUpdateDto.builder()
                    .gameType(slotTeamUser.getSlot().getType())
                    .Ppp(slotTeamUser.getUser().getPpp() + pppChange)
                    .intraId(slotTeamUser.getUser().getIntraId())
                    .isWin(isWin)
                    .build();
            teamService.modifyGameResultInTeam(teamModifyGameResultDto);
            rankRedisService.updateUserPpp(rankUpdateDto);
            userService.modifyUserPpp(modifyPppDto);
            pChangeService.addPChange(pChangeAddDto);
        }
    }

    private void removeCurrentMatch(GameDto game) {
    CurrentMatchFindDto findDto = CurrentMatchFindDto.builder()
            .game(game)
            .build();
    List<CurrentMatchDto> currentMatchDtos = currentMatchService.findCurrentMatchByGame(findDto);
    currentMatchDtos.forEach(currentMatchDto -> {
            CurrentMatchRemoveDto removeDto = CurrentMatchRemoveDto.builder()
                    .userId(currentMatchDto.getUserId()).build();
            currentMatchService.removeCurrentMatch(removeDto);
        });
    }

    private void putResultInGames(List<GameResultDto> gameResultList, List<GameDto> gameLists, UserDto curUser) {
        SlotDto slot;
        List<SlotTeamUserDto> slotTeamUserDtos;
        UserDto leftUser;

        /*
        * GameResultDto -> GameTeamDto -> GamePlayerDto
        * */
        for (GameDto game : gameLists) {
            slot = game.getSlot();
            GameTeamDto gameTeamDto1;
            GameTeamDto gameTeamDto2;
            List<GamePlayerDto> myTeamPlayerDtos = new ArrayList<>();
            List<GamePlayerDto> enemyTeamPlayerDtos = new ArrayList<>();
            Integer team1Score = 0;
            Integer team2Score = 0;
            Boolean isWin = null;
            TeamPosDto teamPosDto = null;

            slotTeamUserDtos = slotTeamUserService.findAllBySlotId(slot.getId());

            if (curUser == null) {
                leftUser = slotTeamUserDtos.get(0).getUser();
            } else {
                leftUser = curUser;
            }

            teamPosDto = teamService.findUsersByTeamPos(slot, leftUser);
            for (SlotTeamUserDto slotTeamUser : slotTeamUserDtos) {
                RankUserDto rankUserDto = rankRedisService.findRankById(RankFindDto.builder()
                        .gameType(slot.getType())
                        .intraId(slotTeamUser.getUser().getIntraId()).build());
                PChangeDto pChangeDto = pChangeService.findPChangeByUserAndGame(PChangeFindDto.builder()
                        .gameId(game.getId())
                        .userId(slotTeamUser.getUser().getIntraId()).build());

                /* 왼 쪽 정 렬 */
                if (teamPosDto.getMyTeam().contains(leftUser.getIntraId())) {
                    //왼쪽에 넣는다.
                    isWin = slotTeamUser.getTeam().getWin();
                    team1Score = slotTeamUser.getTeam().getScore();

                    myTeamPlayerDtos.add(GamePlayerDto.builder()
                            .intraId(slotTeamUser.getUser().getIntraId())
                            .userImageUri(slotTeamUser.getUser().getImageUri())
                            .wins(rankUserDto.getWins())
                            .losses(rankUserDto.getLosses())
                            .winRate(rankUserDto.getWinRate())
                            .pppChange(pChangeDto.getPppChange())
                            .build());
                } else {
                    //오른쪽에 넣는다.
                    isWin = !slotTeamUser.getTeam().getWin();
                    team2Score = slotTeamUser.getTeam().getScore();

                    enemyTeamPlayerDtos.add(GamePlayerDto.builder()
                            .intraId(slotTeamUser.getUser().getIntraId())
                            .userImageUri(slotTeamUser.getUser().getImageUri())
                            .wins(rankUserDto.getWins())
                            .losses(rankUserDto.getLosses())
                            .winRate(rankUserDto.getWinRate())
                            .pppChange(pChangeDto.getPppChange())
                            .build());
                }
            }
            gameTeamDto1 = GameTeamDto.builder()
                    .isWin(isWin)
                    .players(myTeamPlayerDtos)
                    .score(team1Score)
                    .build();
            gameTeamDto2 = GameTeamDto.builder()
                    .isWin(!isWin)
                    .players(enemyTeamPlayerDtos)
                    .score(team2Score)
                    .build();

            gameResultList.add(GameResultDto.builder()
                    .gameId(game.getId())
                    .team1(gameTeamDto1)
                    .team2(gameTeamDto2)
                    .type(game.getSlot().getType())
                    .status(game.getStatus())
                    .time(game.getSlot().getTime())
                    .build());
        }
    }

    private void checkEvent(GameDto game) throws MessagingException {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        if (random.nextInt() % 100 == 42){
            addEventUser(game.getTeam1().getUser1(), "랜덤 당첨 이벤트");
            addEventUser(game.getTeam1().getUser2(), "랜덤 당첨 이벤트");
            addEventUser(game.getTeam2().getUser1(), "랜덤 당첨 이벤트");
            addEventUser(game.getTeam2().getUser2(), "랜덤 당첨 이벤트");
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
