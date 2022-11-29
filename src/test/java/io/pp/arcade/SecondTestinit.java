package io.pp.arcade;

import io.pp.arcade.v1.domain.currentmatch.CurrentMatchRepository;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.noti.NotiRepository;
import io.pp.arcade.v1.domain.pchange.PChangeRepository;
import io.pp.arcade.v1.domain.rank.RankRepository;
import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.SeasonRepository;
import io.pp.arcade.v1.domain.security.jwt.Token;
import io.pp.arcade.v1.domain.security.jwt.TokenRepository;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUser;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.team.TeamRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.RacketType;
import io.pp.arcade.v1.global.type.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SecondTestinit {

    @Autowired
    CurrentMatchRepository currentMatchRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    NotiRepository notiRepository;
    @Autowired
    PChangeRepository pChangeRepository;
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

    public User[] users;
    public Token[] tokens;
    public Team[] teams;
    public Slot[] slots;
    public SlotTeamUser[] slotTeamUser;

    public RankRedis[] ranks;
    public void letInit() {
        users = new User[12];
        users[0] = userRepository.save(User.builder().intraId("hakim").eMail("hihihoho").imageUri("hakim.jpg").statusMessage("kikikaka").ppp(1040).roleType(RoleType.ADMIN).racketType(RacketType.SHAKEHAND).totalExp(1000).build());
        users[1] = userRepository.save(User.builder().intraId("nheo").eMail("hihihoho").imageUri("neho.jpg").statusMessage("kikikaka").ppp(1030).roleType(RoleType.USER).racketType(RacketType.SHAKEHAND).totalExp(900).build());
        users[2] = userRepository.save(User.builder().intraId("donghyuk").eMail("hihihoho").imageUri("null").statusMessage("kikikaka").ppp(1020).roleType(RoleType.USER).racketType(RacketType.SHAKEHAND).totalExp(800).build());
        users[3] = userRepository.save(User.builder().intraId("jiyun").eMail("hihihoho").imageUri("null").statusMessage("kikikaka").ppp(1010).roleType(RoleType.USER).racketType(RacketType.SHAKEHAND).totalExp(700).build());
        users[4] = userRepository.save(User.builder().intraId("jekim").eMail("hihihoho").imageUri("jekim.jpg").statusMessage("kikikaka").ppp(990).roleType(RoleType.USER).racketType(RacketType.SHAKEHAND).totalExp(600).build());
        users[5] = userRepository.save(User.builder().intraId("wochae").eMail("hihihoho").imageUri("wochae.jpg").statusMessage("kikikaka").ppp(980).roleType(RoleType.USER).racketType(RacketType.SHAKEHAND).totalExp(500).build());
        users[6] = userRepository.save(User.builder().intraId("jabae").eMail("hihihoho").imageUri("jabae.jpg").statusMessage("kikikaka").ppp(1000).roleType(RoleType.USER).racketType(RacketType.SHAKEHAND).totalExp(400).build());
        users[7] = userRepository.save(User.builder().intraId("jihyukim").eMail("hihihoho").imageUri("jihyukim.jpg").statusMessage("kikikaka").ppp(992).roleType(RoleType.USER).racketType(RacketType.SHAKEHAND).totalExp(300).build());
        users[8] = userRepository.save(User.builder().intraId("daekim").eMail("hihihoho").imageUri("null").statusMessage("kikikaka").ppp(996).roleType(RoleType.USER).racketType(RacketType.SHAKEHAND).totalExp(200).build());
        users[9] = userRepository.save(User.builder().intraId("sujpark").eMail("hihihoho").imageUri("null").statusMessage("kikikaka").ppp(994).roleType(RoleType.USER).racketType(RacketType.SHAKEHAND).totalExp(100).build());
        users[10] = userRepository.save(User.builder().intraId("kipark").eMail("hihihoho").imageUri("null").statusMessage("kikikaka").ppp(100).roleType(RoleType.USER).racketType(RacketType.SHAKEHAND).totalExp(1).build());
        users[11] = userRepository.save(User.builder().intraId("jujeon").eMail("hihi").imageUri("null").statusMessage("kiki").ppp(1000).roleType(RoleType.ADMIN).racketType(RacketType.DUAL).totalExp(0).build());
        tokens = new Token[12];
        for (Integer i = 0; i < 12; i++) {
            tokens[i] = tokenRepository.save(new Token(users[i], i.toString(), i.toString()));
        }

        /*ranks = new RankRedis[users.length * GameType.values().length];
        List<User> userList = Arrays.stream(users).collect(Collectors.toList());
        for (User user : userList) {
            int idx = userList.indexOf(user);
            RankRedis singleRank = RankRedis.from(UserDto.from(user), GameType.SINGLE);
            RankRedis doubleRank = RankRedis.from(UserDto.from(user), GameType.DOUBLE);

            ranks[idx] = singleRank;
            ranks[users.length + idx] = doubleRank;
            redisTemplate.opsForValue().set(getUserKey(user.getIntraId(), GameType.SINGLE), singleRank);
            redisTemplate.opsForValue().set(getUserKey(user.getIntraId(), GameType.DOUBLE), doubleRank);

            redisTemplate.opsForZSet().add(getRankKey(GameType.SINGLE), getUserRankKey(user.getIntraId(), GameType.SINGLE), user.getPpp());
            redisTemplate.opsForZSet().add(getRankKey(GameType.DOUBLE), getUserRankKey(user.getIntraId(), GameType.DOUBLE), user.getPpp());
        }*/

        slots = new Slot[12];
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        for (int i = 0; i < 12; i++) {
            LocalDateTime time = LocalDateTime.of(tomorrow.getYear(), tomorrow.getMonth(), tomorrow.getDayOfMonth(),
                    15 + i / 6, (i * 15) % 60, 0); // 3시부터 10분 간격으로 18개 슬롯 생성 // 3시부터 15분 간격 12개 슬롯 생성
            slots[i] = slotRepository.save(Slot.builder().tableId(1).headCount(0).time(time).mode(Mode.BOTH).build());
        }

        teams = new Team[36];
        teams[0] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[0]).headCount(0).score(0).build());
        teams[1] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[0]).headCount(0).score(0).build());
        teams[2] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[1]).headCount(0).score(0).build());
        teams[3] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[1]).headCount(0).score(0).build());
        teams[4] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[2]).headCount(0).score(0).build());
        teams[5] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[2]).headCount(0).score(0).build());
        teams[6] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[3]).headCount(0).score(0).build());
        teams[7] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[3]).headCount(0).score(0).build());
        teams[8] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[4]).headCount(0).score(0).build());
        teams[9] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[4]).headCount(0).score(0).build());
        teams[10] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[5]).headCount(0).score(0).build());
        teams[11] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[5]).headCount(0).score(0).build());
        teams[12] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[6]).headCount(0).score(0).build());
        teams[13] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[6]).headCount(0).score(0).build());
        teams[14] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[7]).headCount(0).score(0).build());
        teams[15] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[7]).headCount(0).score(0).build());
        teams[16] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[8]).headCount(0).score(0).build());
        teams[17] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[8]).headCount(0).score(0).build());
        teams[18] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[9]).headCount(0).score(0).build());
        teams[19] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[9]).headCount(0).score(0).build());
        teams[20] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[10]).headCount(0).score(0).build());
        teams[21] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[10]).headCount(0).score(0).build());
        teams[22] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[11]).headCount(0).score(0).build());
        teams[23] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[11]).headCount(0).score(0).build());
//        teams[24] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[12]).headCount(0).score(0).build());
//        teams[25] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[12]).headCount(0).score(0).build());
//        teams[26] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[13]).headCount(0).score(0).build());
//        teams[27] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[13]).headCount(0).score(0).build());
//        teams[28] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[14]).headCount(0).score(0).build());
//        teams[29] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[14]).headCount(0).score(0).build());
//        teams[30] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[15]).headCount(0).score(0).build());
//        teams[31] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[15]).headCount(0).score(0).build());
//        teams[32] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[16]).headCount(0).score(0).build());
//        teams[33] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[16]).headCount(0).score(0).build());
//        teams[34] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[17]).headCount(0).score(0).build());
//        teams[35] = teamRepository.save(Team.builder().teamPpp(0).slot(slots[17]).headCount(0).score(0).build());

        Season testSeason = seasonRepository.save(Season.builder().seasonName("Test").startTime(LocalDateTime.now().minusYears(1)).endTime(LocalDateTime.now().plusYears(1)).startPpp(1000).pppGap(150).build());
        slotTeamUser = new SlotTeamUser[24];
//        slotTeamUser[0] = slotTeamUserRepository.save(SlotTeamUser.builder().team(teams[0]).slot(slots[0]).user(users[0]).build());
//        slotTeamUser[1] = slotTeamUserRepository.save(SlotTeamUser.builder().team(teams[1]).slot(slots[0]).user(users[1]).build());

        int i = 0;
        for(Slot s : slots){
            slotTeamUser[i] = slotTeamUserRepository.save(SlotTeamUser.builder().team(teams[i]).slot(s).user(users[i % users.length]).build());
            slotTeamUser[i + 1] = slotTeamUserRepository.save(SlotTeamUser.builder().team(teams[i + 1]).slot(s).user(users[(i + 1) % users.length]).build());
            i += 2;
        }
    }
}