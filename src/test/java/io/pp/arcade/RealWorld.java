package io.pp.arcade;

import io.pp.arcade.v1.domain.currentmatch.CurrentMatch;
import io.pp.arcade.v1.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.noti.Noti;
import io.pp.arcade.v1.domain.noti.NotiRepository;
import io.pp.arcade.v1.domain.pchange.PChange;
import io.pp.arcade.v1.domain.pchange.PChangeRepository;
import io.pp.arcade.v1.domain.rank.RankRedisRepository;
import io.pp.arcade.v1.domain.rank.RankRepository;
import io.pp.arcade.v1.domain.rank.RedisKeyManager;
import io.pp.arcade.v1.domain.rank.dto.RankKeyGetDto;
import io.pp.arcade.v1.domain.rank.dto.RedisRankAddDto;
import io.pp.arcade.v1.domain.rank.dto.RedisRankUpdateDto;
import io.pp.arcade.v1.domain.rank.dto.RedisRankingAddDto;
import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.SeasonRepository;
import io.pp.arcade.v1.domain.security.jwt.Token;
import io.pp.arcade.v1.domain.security.jwt.TokenRepository;
import io.pp.arcade.v1.domain.security.oauth.v2.token.AuthToken;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUser;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.team.TeamRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.redis.Key;
import io.pp.arcade.v1.global.type.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static io.pp.arcade.v1.global.type.GameType.SINGLE;

@Component
public class RealWorld {
    @Autowired
    CurrentMatchRepository currentMatchRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    NotiRepository notiRepository;
    @Autowired
    PChangeRepository pChangeRepository;
    @Autowired
    RankRedisRepository rankRedisRepository;
    @Autowired
    RedisKeyManager redisKeyManager;
    @Autowired
    RankRepository rankRepository;
    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    SlotRepository slotRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SlotTeamUserRepository slotTeamUserRepository;
    @Autowired
    RedisTemplate<String, String> redisRank;
    @Autowired
    RedisTemplate<String, RankRedis> redisUser;


    private final String defaultUrl = "https://42gg-public-image.s3.ap-northeast-2.amazonaws.com/images/small_default.jpeg";
    private final LocalDateTime now = LocalDateTime.now();
    private Integer createdUserCount = 0;
    private Integer baseImminentTime = 5;
    private Integer baseIntervalTime = 10;

    public void makeMixedGameResultsForYesterday(Season season) {
        Slot[] slots;
        Team[] teams;
        User[] users;

        if (!(season.getStartTime().isBefore(now) && season.getEndTime().isAfter(now))) {
            return;
        }

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        users = makeDeafultUsers();
        slots = makeSlotsByTime(yesterday);
        teams = makeTeamsBySlots(slots);
        makeEndGames(season, slots, teams, users);
        // NOTI..? ㅠ ㅠ
    }

    public void makeMixedGameResultsForDayAmongPastSeason(Season season, Integer dayAfterSeasonStart) {
        Slot[] slots;
        Team[] teams;
        User[] users;
        if (!(season.getStartTime().isBefore(now) && season.getEndTime().isBefore(now.plusDays(dayAfterSeasonStart)))) {
            return;
        }
        LocalDateTime day = season.getStartTime().plusDays(dayAfterSeasonStart);
        users = makeDeafultUsers();
        slots = makeSlotsByTime(day);
        teams = makeTeamsBySlots(slots);
        makeEndGames(season, slots, teams, users);
        // NOTI..? ㅠ ㅠ
    }

    public User getUserWinRateInfiniteDecimal(){
        User user = userRepository.save(User.builder()
                .intraId("infiniteDecimal")
                .eMail("infiniteDecimal" + "@42gg.kr")
                .imageUri(defaultUrl)
                .racketType(RacketType.DUAL)
                .statusMessage("Hello, I'm infiniteDecimal")
                .ppp(1000)
                .totalExp(3000)
                .roleType(RoleType.USER)
                .build());
        makeTokenForUser(user);

        RankRedis userRank = addRankRedisByUser(user);

        userRank.update(1, 0);
        userRank.update(1, 0);
        userRank.update(0, 0);

        String curSeason = redisKeyManager.getCurrentRankKey();
        rankRedisRepository.updateRank(RedisRankUpdateDto.builder().userRank(userRank).userId(user.getId()).seasonKey(curSeason).build());
        return user;
    }

    public User getUserWinRateFiniteDecimal() {
        User user = userRepository.save(User.builder()
                .intraId("finiteDecimal")
                .eMail("finiteDecimal" + "@42gg.kr")
                .imageUri(defaultUrl)
                .racketType(RacketType.DUAL)
                .statusMessage("Hello, I'm finiteDecimal")
                .ppp(1000)
                .totalExp(2000)
                .roleType(RoleType.USER)
                .build());
        makeTokenForUser(user);

        RankRedis userRank = addRankRedisByUser(user);

        userRank.update(1, 0);
        userRank.update(1, 0);
        userRank.update(0, 0);
        userRank.update(0, 0);

        String curSeason = redisKeyManager.getCurrentRankKey();
        rankRedisRepository.updateRank(RedisRankUpdateDto.builder().userRank(userRank).userId(user.getId()).seasonKey(curSeason).build());
        return user;
    }

    public User getUserWinRateZero() {
        User user = userRepository.save(User.builder()
                .intraId("zero")
                .eMail("zero" + "@42gg.kr")
                .imageUri(defaultUrl)
                .racketType(RacketType.DUAL)
                .statusMessage("Hello, I'm zero")
                .ppp(1000)
                .totalExp(1000)
                .roleType(RoleType.USER)
                .build());
        makeTokenForUser(user);

        RankRedis userRank = addRankRedisByUser(user);

        userRank.update(0, 0);
        userRank.update(0, 0);
        userRank.update(0, 0);
        userRank.update(0, 0);

        String curSeason = redisKeyManager.getCurrentRankKey();
        rankRedisRepository.updateRank(RedisRankUpdateDto.builder().userRank(userRank).userId(user.getId()).seasonKey(curSeason).build());
        return user;
    }

    public User getUserStatusMessageDifferentEverySeason() {
        User user = userRepository.save(User.builder()
                .intraId("StatusMessageBySeason")
                .eMail("StatusMessageBySeason" + "@42gg.kr")
                .imageUri(defaultUrl)
                .racketType(RacketType.DUAL)
                .statusMessage("Hello, I have message for every season")
                .ppp(1000)
                .totalExp(1000)
                .roleType(RoleType.USER)
                .build());
        makeTokenForUser(user);
        List<Season> seasons = seasonRepository.findAll();
        seasons.forEach(season -> {
            user.setStatusMessage(season.getSeasonName());
            addRankRedisByUser(user, season);
        });
        return user;
    }

    public User basicUser() {
        User user = userRepository.save(User.builder()
                .intraId("basic" + createdUserCount)
                .eMail("basic" + createdUserCount + "@42gg.kr")
                .imageUri(defaultUrl)
                .racketType(RacketType.DUAL)
                .statusMessage("Hello, I'm basic user number " + createdUserCount)
                .ppp(1000)
                .totalExp(1000)
                .roleType(RoleType.USER)
                .build());
        makeTokenForUser(user);
        addRankRedisByUser(user);
        createdUserCount++;
        return user;
    }

    public User getUserWithNoCurrentMatch() {
        User user = userRepository.save(User.builder()
                .intraId("noCurrentMatch")
                .eMail("noCurrentMatch" + "@42gg.kr")
                .imageUri(defaultUrl)
                .racketType(RacketType.DUAL)
                .statusMessage("Hello, I don't have any current match number")
                .ppp(1000)
                .totalExp(1000)
                .roleType(RoleType.USER)
                .build());
        makeTokenForUser(user);
        addRankRedisByUser(user);
        return user;
    }

    public User getUserWithVariousNotis() {
        User user = userRepository.save(User.builder()
                .intraId("variousNotis")
                .eMail("variousNotis" + "@42gg.kr")
                .imageUri(defaultUrl)
                .racketType(RacketType.DUAL)
                .statusMessage("Hello, I have various notifications")
                .ppp(1000)
                .totalExp(1000)
                .roleType(RoleType.USER)
                .build());
        makeTokenForUser(user);
        addRankRedisByUser(user);
        for (NotiType notiType : NotiType.values()) {
            notiRepository.save(Noti.builder()
                    .slot(null)
                    .user(user)
                    .type(notiType)
                    .message("Test Code So Fun Happy Happy Emart")
                    .isChecked(false)
                    .build());
        }
        return user;
    }

    public CurrentMatch makeUserHaveCurrentMatchNotMatched(Mode mode, User user) {
        Slot slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .time(now.plusMinutes(10))
                .gamePpp(user.getPpp())
                .headCount(1)
                .type(GameType.SINGLE)
                .mode(mode)
                .build());
        Team userTeam = teamRepository.save(Team.builder()
                .teamPpp(user.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());
        Team emptyTeam = teamRepository.save(Team.builder()
                .teamPpp(0)
                .headCount(0)
                .score(0)
                .slot(slot)
                .build());

        SlotTeamUser slotTeamUser = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(userTeam)
                .user(user)
                .build());
        CurrentMatch currentMatch = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user)
                .isMatched(false)
                .matchImminent(false)
                .isDel(false)
                .build());
        return currentMatch;
    }


    public CurrentMatch[] makeUsersHaveCurrentMatchMatchedNotImminent(Mode mode, User user1, User user2) {
        Slot slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .time(now.plusMinutes(baseImminentTime * 2))
                .gamePpp((user1.getPpp() + user2.getPpp()) / 2)
                .headCount(2)
                .type(GameType.SINGLE)
                .mode(mode)
                .build());
        Team user1Team = teamRepository.save(Team.builder()
                .teamPpp(user1.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());
        Team user2Team = teamRepository.save(Team.builder()
                .teamPpp(user2.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());

        SlotTeamUser slotTeamUser1 = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(user1Team)
                .user(user1)
                .build());
        SlotTeamUser slotTeamUser2 = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(user2Team)
                .user(user2)
                .build());
        CurrentMatch currentMatch1 = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user1)
                .isMatched(true)
                .matchImminent(false)
                .isDel(false)
                .build());
        CurrentMatch currentMatch2 = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user2)
                .isMatched(true)
                .matchImminent(false)
                .isDel(false)
                .build());
        return new CurrentMatch[]{currentMatch1, currentMatch2};
    }

    public CurrentMatch[] makeUsersHaveCurrentMatchMatchedImminentGameNotStarted(Mode mode, User user1, User user2) {
        Slot slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .time(now.plusMinutes(baseImminentTime).minusSeconds(1))
                .gamePpp((user1.getPpp() + user2.getPpp()) / 2)
                .headCount(2)
                .type(GameType.SINGLE)
                .mode(mode)
                .build());
        Team user1Team = teamRepository.save(Team.builder()
                .teamPpp(user1.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());
        Team user2Team = teamRepository.save(Team.builder()
                .teamPpp(user2.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());

        SlotTeamUser slotTeamUser1 = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(user1Team)
                .user(user1)
                .build());
        SlotTeamUser slotTeamUser2 = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(user2Team)
                .user(user2)
                .build());
        CurrentMatch currentMatch1 = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user1)
                .isMatched(true)
                .matchImminent(true)
                .isDel(false)
                .build());
        CurrentMatch currentMatch2 = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user2)
                .isMatched(true)
                .matchImminent(true)
                .isDel(false)
                .build());
        return new CurrentMatch[]{currentMatch1, currentMatch2};
    }

    public CurrentMatch[] makeUsersHaveCurrentMatchMatchedImminentGameStatusLive(Mode mode, Season season, User user1, User user2) {
        Slot slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .time(now.minusSeconds(1))
                .gamePpp((user1.getPpp() + user2.getPpp()) / 2)
                .headCount(2)
                .type(GameType.SINGLE)
                .mode(mode)
                .build());
        Team user1Team = teamRepository.save(Team.builder()
                .teamPpp(user1.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());
        Team user2Team = teamRepository.save(Team.builder()
                .teamPpp(user2.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());

        SlotTeamUser slotTeamUser1 = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(user1Team)
                .user(user1)
                .build());
        SlotTeamUser slotTeamUser2 = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(user2Team)
                .user(user2)
                .build());
        CurrentMatch currentMatch1 = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user1)
                .isMatched(true)
                .matchImminent(true)
                .isDel(false)
                .build());
        CurrentMatch currentMatch2 = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user2)
                .isMatched(true)
                .matchImminent(true)
                .isDel(false)
                .build());
        Game game = gameRepository.save(Game.builder()
                .slot(slot)
                .season(season.getId())
                .status(StatusType.LIVE)
                .mode(mode)
                .type(GameType.SINGLE)
                .time(slot.getTime())
                .build());
        return new CurrentMatch[]{currentMatch1, currentMatch2};
    }

    public CurrentMatch[] makeUsersHaveCurrentMatchMatchedImminentGameStatusWait(Mode mode, Season season, User user1, User user2) {
        Slot slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .time(now.minusMinutes(baseIntervalTime).minusSeconds(1))
                .gamePpp((user1.getPpp() + user2.getPpp()) / 2)
                .headCount(2)
                .type(GameType.SINGLE)
                .mode(mode)
                .build());
        Team user1Team = teamRepository.save(Team.builder()
                .teamPpp(user1.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());
        Team user2Team = teamRepository.save(Team.builder()
                .teamPpp(user2.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());

        SlotTeamUser slotTeamUser1 = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(user1Team)
                .user(user1)
                .build());
        SlotTeamUser slotTeamUser2 = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(user2Team)
                .user(user2)
                .build());
        CurrentMatch currentMatch1 = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user1)
                .isMatched(true)
                .matchImminent(true)
                .isDel(false)
                .build());
        CurrentMatch currentMatch2 = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user2)
                .isMatched(true)
                .matchImminent(true)
                .isDel(false)
                .build());
        Game game = gameRepository.save(Game.builder()
                .slot(slot)
                .season(season.getId())
                .status(StatusType.WAIT)
                .mode(mode)
                .type(GameType.SINGLE)
                .time(slot.getTime())
                .build());
        return new CurrentMatch[]{currentMatch1, currentMatch2};
    }

    public CurrentMatch[] makeUsersHaveCurrentMatchDeletedGameStatusEnd(Mode mode, Season season, User winUser, User loseUser, Integer winUserScore, Integer loseUserScore) {
        Slot slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .time(now.minusMinutes(baseIntervalTime).minusSeconds(1))
                .gamePpp((winUser.getPpp() + loseUser.getPpp()) / 2)
                .headCount(2)
                .type(GameType.SINGLE)
                .mode(mode)
                .build());
        Team winTeam = teamRepository.save(Team.builder()
                .teamPpp(winUser.getPpp())
                .headCount(1)
                .score(winUserScore)
                .slot(slot)
                .build());
        Team loseTeam = teamRepository.save(Team.builder()
                .teamPpp(loseUser.getPpp())
                .headCount(1)
                .score(loseUserScore)
                .slot(slot)
                .build());

        SlotTeamUser slotTeamUserWon = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(winTeam)
                .user(winUser)
                .build());
        SlotTeamUser slotTeamUserLost = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(loseTeam)
                .user(loseUser)
                .build());
        CurrentMatch currentMatch1 = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(winUser)
                .isMatched(true)
                .matchImminent(true)
                .isDel(true)
                .build());
        CurrentMatch currentMatch2 = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(loseUser)
                .isMatched(true)
                .matchImminent(true)
                .isDel(true)
                .build());
        Game game = gameRepository.save(Game.builder()
                .slot(slot)
                .season(season.getId())
                .status(StatusType.END)
                .mode(mode)
                .type(GameType.SINGLE)
                .time(slot.getTime())
                .build());
        pChangeRepository.save(PChange.builder()
                .game(game)
                .user(winUser)
                .pppChange(20)
                .pppResult(winUser.getPpp() + 20)
                .expChange(100)
                .expResult(winUser.getTotalExp() + 100)
                .build());
        pChangeRepository.save(PChange.builder()
                .game(game)
                .user(loseUser)
                .pppChange(-20)
                .pppResult(loseUser.getPpp() - 20)
                .expChange(100)
                .expResult(loseUser.getTotalExp() + 100)
                .build());
        winUser.setPpp(winUser.getPpp() + 20);
        winUser.setTotalExp(winUser.getTotalExp() + 100);

        loseUser.setPpp(loseUser.getPpp() - 20);
        loseUser.setTotalExp(loseUser.getTotalExp() - 100);

        String redisSeason = redisKeyManager.getSeasonKey(season.getId().toString(), season.getSeasonName());

        RankRedis userRank = addRankRedisByUser(winUser, season);
        userRank.update(booleanToInt(winTeam.getWin()), winUser.getPpp());
        rankRedisRepository.updateRank(RedisRankUpdateDto.builder().userRank(userRank).userId(winUser.getId()).seasonKey(redisSeason).build());

        RankRedis userRank2 = addRankRedisByUser(loseUser, season);
        userRank2.update(booleanToInt(loseTeam.getWin()), loseUser.getPpp());
        rankRedisRepository.updateRank(RedisRankUpdateDto.builder().userRank(userRank2).userId(loseUser.getId()).seasonKey(redisSeason).build());
        return new CurrentMatch[]{currentMatch1, currentMatch2};
    }

    public Slot getEmptySlotMinutesLater(Integer minutes) {
        Slot slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .time(now.plusMinutes(minutes))
                .headCount(0)
                .mode(Mode.BOTH)
                .build());

        teamRepository.save(Team.builder()
                .teamPpp(0)
                .headCount(0)
                .score(0)
                .slot(slot)
                .build());

        teamRepository.save(Team.builder()
                .teamPpp(0)
                .headCount(0)
                .score(0)
                .slot(slot)
                .build());
        return slot;
    }

    public Slot getExpiredSlot() {
        Slot slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .time(now.minusMinutes(100))
                .headCount(0)
                .mode(Mode.BOTH)
                .build());

        teamRepository.save(Team.builder()
                .teamPpp(0)
                .headCount(0)
                .score(0)
                .slot(slot)
                .build());

        teamRepository.save(Team.builder()
                .teamPpp(0)
                .headCount(0)
                .score(0)
                .slot(slot)
                .build());
        return slot;
    }

    public Slot getNormalSlotWithOneUserMinutesLater(User user, Integer minutes) {
        Slot slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .time(now.plusMinutes(minutes))
                .gamePpp(user.getPpp())
                .headCount(1)
                .type(GameType.SINGLE)
                .mode(Mode.NORMAL)
                .build());
        Team userTeam = teamRepository.save(Team.builder()
                .teamPpp(user.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());
        Team emptyTeam = teamRepository.save(Team.builder()
                .teamPpp(0)
                .headCount(0)
                .score(0)
                .slot(slot)
                .build());

        SlotTeamUser slotTeamUser = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(userTeam)
                .user(user)
                .build());
        CurrentMatch currentMatch = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user)
                .isMatched(false)
                .matchImminent(false)
                .isDel(false)
                .build());
        return slot;
    }

    public Slot getNormalSlotWithTwoUsersMinutesLater(User user1, User user2, Integer minutes) {
        Slot slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .time(now.plusMinutes(minutes))
                .gamePpp((user1.getPpp() + user2.getPpp()) / 2)
                .headCount(2)
                .type(GameType.SINGLE)
                .mode(Mode.NORMAL)
                .build());
        Team user1Team = teamRepository.save(Team.builder()
                .teamPpp(user1.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());
        Team user2Team = teamRepository.save(Team.builder()
                .teamPpp(user2.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());

        SlotTeamUser slotTeamUser1 = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(user1Team)
                .user(user1)
                .build());
        SlotTeamUser slotTeamUser2 = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(user2Team)
                .user(user2)
                .build());
        CurrentMatch currentMatch1 = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user1)
                .isMatched(true)
                .matchImminent(minutes > baseImminentTime)
                .isDel(false)
                .build());
        CurrentMatch currentMatch2 = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user2)
                .isMatched(true)
                .matchImminent(minutes > baseImminentTime)
                .isDel(false)
                .build());
        return slot;
    }

    public Slot getRankedSlotWithOneUserMinutesLater(User user, Integer minutes) {
        Slot slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .time(now.plusMinutes(minutes))
                .gamePpp(user.getPpp())
                .headCount(1)
                .type(GameType.SINGLE)
                .mode(Mode.RANK)
                .build());
        Team userTeam = teamRepository.save(Team.builder()
                .teamPpp(user.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());
        Team emptyTeam = teamRepository.save(Team.builder()
                .teamPpp(0)
                .headCount(0)
                .score(0)
                .slot(slot)
                .build());

        SlotTeamUser slotTeamUser = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(userTeam)
                .user(user)
                .build());
        CurrentMatch currentMatch = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user)
                .isMatched(false)
                .matchImminent(false)
                .isDel(false)
                .build());
        return slot;
    }

    public Slot getRankedSlotWithTwoUsersMinutesLater(User user1, User user2, Integer minutes) {
        Slot slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .time(now.plusMinutes(minutes))
                .gamePpp((user1.getPpp() + user2.getPpp()) / 2)
                .headCount(2)
                .type(GameType.SINGLE)
                .mode(Mode.RANK)
                .build());
        Team user1Team = teamRepository.save(Team.builder()
                .teamPpp(user1.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());
        Team user2Team = teamRepository.save(Team.builder()
                .teamPpp(user2.getPpp())
                .headCount(1)
                .score(0)
                .slot(slot)
                .build());

        SlotTeamUser slotTeamUser1 = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(user1Team)
                .user(user1)
                .build());
        SlotTeamUser slotTeamUser2 = slotTeamUserRepository.save(SlotTeamUser.builder()
                .slot(slot)
                .team(user2Team)
                .user(user2)
                .build());
        CurrentMatch currentMatch1 = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user1)
                .isMatched(true)
                .matchImminent(minutes > baseImminentTime)
                .isDel(false)
                .build());
        CurrentMatch currentMatch2 = currentMatchRepository.save(CurrentMatch.builder()
                .slot(slot)
                .user(user2)
                .isMatched(true)
                .matchImminent(minutes > baseImminentTime)
                .isDel(false)
                .build());
        return slot;
    }

    public User[] getUsersWithVariousPPP() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        User[] users = new User[50];
        for (int i = 0; i < 50; i++) {
            Integer randInt = (random.nextInt() % 400) - 200;
            users[i] = userRepository.save(User.builder()
                    .intraId("ppp" + randInt)
                    .eMail(randInt + "@42gg.kr")
                    .imageUri(defaultUrl)
                    .racketType(RacketType.values()[i % 2])
                    .statusMessage("Hello, my ppp is " + randInt)
                    .ppp(1000 + randInt)
                    .totalExp(100 * i)
                    .roleType(RoleType.USER)
                    .build());
            makeTokenForUser(users[i]);
            addRankRedisByUser(users[i]);
        }
        return users;
    }

    public User[] getAdminUsers() {
        User[] users = new User[10];
        for (int i = 0; i < 10; i++) {
            users[i] = userRepository.save(User.builder()
                    .intraId("admin" + i)
                    .eMail("admin" + i + "@42gg.kr")
                    .imageUri(defaultUrl)
                    .racketType(RacketType.values()[i % 2])
                    .statusMessage("Hello, I'm admin " + i)
                    .ppp(1000)
                    .totalExp(100 * i)
                    .roleType(RoleType.ADMIN)
                    .build());
            makeTokenForUser(users[i]);
            addRankRedisByUser(users[i]);
        }
        return users;
    }

//    public User[] getGuestUsers() {
//        // 추후 게스트 양식 정해지는대로 작성
//    }

    private User[] makeDeafultUsers() {
        User[] users = new User[10];
        for (int i = 0; i < 10; i++) {
            users[i] = userRepository.save(User.builder()
                    .intraId(Integer.valueOf(i).toString())
                    .eMail(i + "@42gg.kr")
                    .imageUri(defaultUrl)
                    .racketType(RacketType.values()[i % 2])
                    .statusMessage("Hello, I'm " + i)
                    .ppp(1000)
                    .totalExp(100 * i)
                    .roleType(RoleType.USER)
                    .build());
            makeTokenForUser(users[i]);
            addRankRedisByUser(users[i]);
        }
        return users;
    }

    private Season[] makeDefaultSeasons() {
        Season[] seasons = new Season[3];
        seasons[0] = seasonRepository.save(Season.builder()
                .seasonName("시즌 1 랭크")
                .startTime(now.minusMonths(5))
                .endTime(now.minusMonths(3).minusSeconds(1))
                .startPpp(1000)
                .pppGap(150)
                .seasonMode(Mode.RANK)
                .build());
        seasons[1] = seasonRepository.save(Season.builder()
                .seasonName("시즌 2 노말")
                .startTime(now.minusMonths(3))
                .endTime(now.minusMonths(1).minusSeconds(1))
                .startPpp(1000)
                .pppGap(150)
                .seasonMode(Mode.NORMAL)
                .build());
        seasons[2] = seasonRepository.save(Season.builder()
                .seasonName("시즌 3 혼합")
                .startTime(now.minusMonths(1))
                .endTime(now.plusMonths(1).minusSeconds(1))
                .startPpp(1000)
                .pppGap(150)
                .seasonMode(Mode.BOTH)
                .build());
        return seasons;
    }

    private void makeEndGames(Season season, Slot[] slots, Team[] teams, User[] users) {
        SlotTeamUser[] slotTeamUsers = new SlotTeamUser[20];
        CurrentMatch[] currentMatches = new CurrentMatch[20];

        for (int i = 0; i < slots.length; i++) {
            Game game = gameRepository.save(Game.builder()
                    .slot(slots[i])
                    .season(season.getId())
                    .status(StatusType.END)
                    .mode(i % 2 == 0 ? Mode.RANK : Mode.NORMAL)
                    .type(GameType.SINGLE)
                    .time(slots[i].getTime())
                    .build());
            slotTeamUsers[i * 2] = slotTeamUserRepository.save(SlotTeamUser.builder()
                    .slot(slots[i])
                    .team(teams[i * 2])
                    .user(users[(i * 2) % 10])
                    .build());
            currentMatches[i * 2] = currentMatchRepository.save(CurrentMatch.builder()
                    .slot(slots[i])
                    .user(users[(i * 2) % 10])
                    .isMatched(true)
                    .matchImminent(true)
                    .isDel(true)
                    .build());
            slotTeamUsers[i * 2 + 1] = slotTeamUserRepository.save(SlotTeamUser.builder()
                    .slot(slots[i])
                    .team(teams[i * 2 + 1])
                    .user(users[(i * 2 + 1) % 10])
                    .build());
            currentMatches[i * 2 + 1] = currentMatchRepository.save(CurrentMatch.builder()
                    .slot(slots[i])
                    .user(users[(i * 2 + 1) % 10])
                    .isMatched(true)
                    .matchImminent(true)
                    .isDel(true)
                    .build());
            slots[i].setMode(game.getMode());
            slots[i].setGamePpp((users[(i * 2) % 10].getPpp() + users[(i * 2 + 1) % 10].getPpp()) / 2);
            slots[i].setType(GameType.SINGLE);
            if (i % 2 == 0) {
                teams[i * 2].setScore(2);
                teams[i * 2 + 1].setScore(1);
            }
            pChangeRepository.save(PChange.builder()
                    .game(game)
                    .user(users[(i * 2) % 10])
                    .pppChange(20)
                    .pppResult(users[(i * 2) % 10].getPpp() + 20)
                    .expChange(100)
                    .expResult(users[(i * 2) % 10].getTotalExp() + 100)
                    .build());
            pChangeRepository.save(PChange.builder()
                    .game(game)
                    .user(users[(i * 2 + 1) % 10])
                    .pppChange(-20)
                    .pppResult(users[(i * 2 + 1) % 10].getPpp() - 20)
                    .expChange(100)
                    .expResult(users[(i * 2 + 1) % 10].getTotalExp() + 100)
                    .build());
            users[i * 2].setPpp(users[i * 2].getPpp() + 20);
            users[i * 2].setTotalExp(users[i * 2].getTotalExp() + 100);

            String redisSeason = redisKeyManager.getSeasonKey(season.getId().toString(), season.getSeasonName());

            RankRedis userRank = addRankRedisByUser(users[i * 2], season);
            userRank.update(booleanToInt(teams[i * 2].getWin()), users[i * 2].getPpp());
            rankRedisRepository.updateRank(RedisRankUpdateDto.builder().userRank(userRank).userId(users[i * 2].getId()).seasonKey(redisSeason).build());

            users[i * 2 + 1].setPpp(users[i * 2 + 1].getPpp() + 20);
            users[i * 2 + 1].setTotalExp(users[i * 2 + 1].getTotalExp() + 100);
            RankRedis userRank2 = addRankRedisByUser(users[i * 2 + 1], season);
            userRank2.update(booleanToInt(teams[i * 2 + 1].getWin()), users[i * 2 + 1].getPpp());
            rankRedisRepository.updateRank(RedisRankUpdateDto.builder().userRank(userRank2).userId(users[i * 2 + 1].getId()).seasonKey(redisSeason).build());
        }
    }


    private Team[] makeTeamsBySlots(Slot[] slots) {
        Team[] teams = new Team[slots.length];
        for (int i = 0; i < slots.length; i += 2) {
            teams[i * 2] = teamRepository.save(Team.builder()
                    .slot(slots[i])
                    .teamPpp(0)
                    .headCount(0)
                    .score(0)
                    .build());
            teams[i * 2 + 1] = teamRepository.save(Team.builder()
                    .slot(slots[i + 1])
                    .teamPpp(0)
                    .headCount(0)
                    .score(0)
                    .build());
        }
        return teams;
    }

    private Slot[] makeSlotsByTime(LocalDateTime baseTime) {
        Slot[] slots = new Slot[20];
        for (int i = 0; i < 20; i++) {
            LocalDateTime time = LocalDateTime.of(baseTime.getYear(), baseTime.getMonth(), baseTime.getDayOfMonth(),
                    14 + i / 6, (i * 10) % 60, 0);
            slots[i] = slotRepository.save(Slot.builder()
                    .tableId(1)
                    .time(time)
                    .gamePpp(null)
                    .headCount(0)
                    .type(null)
                    .mode(Mode.BOTH)
                    .build());
        }
        return slots;
    }

    private RankRedis addRankRedisByUser(User user) {
        /* 현재 시즌 정보 */
        LocalDateTime now = LocalDateTime.now();
        Season season = seasonRepository.findSeasonByStartTimeIsBeforeAndEndTimeIsAfter(now,now).orElse(null);

        return addRankRedisByUser(user, season);
    }

    private RankRedis addRankRedisByUser(User user, Season season) {
        UserDto userDto = UserDto.from(user);
        RankRedis userRank = RankRedis.from(userDto, SINGLE);

        RankKeyGetDto rankKeyGetDto = RankKeyGetDto.builder().seasonId(season.getId()).seasonName(season.getSeasonName()).build();
        String rankKey = redisKeyManager.getRankKeyBySeason(rankKeyGetDto);
        RedisRankAddDto redisRankAddDto = RedisRankAddDto.builder().key(rankKey).userId(userDto.getId()).rank(userRank).build();
        rankRedisRepository.addRank(redisRankAddDto);

        String rankingKey = redisKeyManager.getRankKeyBySeason(rankKeyGetDto);
        RedisRankingAddDto redisRankingAddDto = RedisRankingAddDto.builder().rankingKey(rankingKey).rank(userRank).build();
        rankRedisRepository.addRanking(redisRankingAddDto);

        return userRank;
    }

    private int booleanToInt(boolean value) {
        return value ? 1 : 0;
    }

    private Token makeTokenForUser(User user) {
        String uuid = String.valueOf(UUID.randomUUID());
        return tokenRepository.save(new Token(user, uuid, uuid));
    }
}
