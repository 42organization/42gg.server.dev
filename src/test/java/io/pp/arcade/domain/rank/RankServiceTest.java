package io.pp.arcade.domain.rank;

import io.pp.arcade.domain.rank.dto.*;
import io.pp.arcade.domain.rank.service.RankRedisService;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.redis.Key;
import io.pp.arcade.global.type.RacketType;
import io.pp.arcade.global.type.RoleType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;

import javax.transaction.Transactional;

import java.util.HashMap;

@SpringBootTest
class RankServiceTest {
    @Autowired
    private RankRedisRepository rankRedisRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RankRedisService rankRedisService;

    @Autowired
    private RedisTemplate redisTemplate;

    User user;
    User user2;
    User user3;
    User user4;
    User user5;
    User user6;
    HashMap<String, User> users ;

    @BeforeEach
    void init(){
        user = userRepository.save(User.builder().ppp(0).statusMessage("").intraId("donghyuk").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).roleType(RoleType.USER).build());
        user2 = userRepository.save(User.builder().ppp(100).statusMessage("").intraId("nheo").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).roleType(RoleType.USER).build());
        user3 = userRepository.save(User.builder().ppp(200).statusMessage("").intraId("hakim").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).roleType(RoleType.USER).build());
        user4 = userRepository.save(User.builder().ppp(300).statusMessage("").intraId("jiyun").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).roleType(RoleType.USER).build());
        user5 = userRepository.save(User.builder().ppp(400).statusMessage("").intraId("jekim").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).roleType(RoleType.USER).build());
        user6 = userRepository.save(User.builder().ppp(500).statusMessage("").intraId("wochae").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).roleType(RoleType.USER).build());

        users = new HashMap<>();
        users.put("donghyuk", user);
        users.put("nheo", user2);
        users.put("hakim", user3);
        users.put("jiyun", user4);
        users.put("jekim", user5);
        users.put("wochae", user6);
        //rankRepository.save(Rank.builder().user(user).ranking(0).losses(0).wins(0).ppp(0).racketType(RacketType.SHAKEHAND).seasonId(0).build());
    }

    @Test
    @Transactional
    void findRankById() {
        // given
        RankFindDto findUserDto =  RankFindDto.builder().intraId(user.getIntraId()).gameType(GameType.valueOf(Key.SINGLE)).build();

        // when
        RankUserDto RankUserRankingDto = rankRedisService.findRankById(findUserDto);
        // then
        Assertions.assertThat(RankUserRankingDto.getStatusMessage()).isEqualTo(findUserDto.getIntraId());
    }

    @Test
    @Transactional
    void addRank() {
        RankAddDto addDto = RankAddDto.builder().userId(user.getId()).ppp(0).build();
        RankModifyDto modifyPppDto = RankModifyDto.builder().isWin(true).intraId(user.getIntraId()).Ppp(100).gameType(GameType.valueOf(Key.SINGLE)).build();
        // when
        //rankServiceImpl.addRank(addDto);
        rankRedisService.modifyUserPpp(modifyPppDto);
        modifyPppDto = RankModifyDto.builder().isWin(true).intraId(user.getIntraId()).Ppp(200).gameType(GameType.valueOf(Key.SINGLE)).build();
        rankRedisService.modifyUserPpp(modifyPppDto);
        rankRedisService.modifyRankStatusMessage(RankModifyStatusMessageDto.builder().statusMessage("message").intraId(user.getIntraId()).build());
        //rankService.modifyRankStatusMessage(RankModifyStatusMessageDto.builder().intraId(user.getIntraId()).statusMessage("상태메시지 수정").gametype(RankKey.SINGLE).build());
        // then
    }

    @Test
    @Transactional
    void modifyRankPpp() {

    }


    @Test
    @Transactional
    void isEmpty(){

        Boolean isEmpty = rankRedisService.isEmpty();
        Assertions.assertThat(isEmpty).isTrue();
    }
    @Test
    @Transactional
    void findRank() {
        // given
        RankRedis singleRank =  RankRedis.from(UserDto.from(user), Key.SINGLE);
        RankRedis doubleRank =  RankRedis.from(UserDto.from(user), Key.BUNGLE);

        redisTemplate.opsForValue().set(user.getIntraId() + Key.SINGLE, singleRank);
        redisTemplate.opsForValue().set(user.getIntraId() + Key.BUNGLE, doubleRank);
        redisTemplate.opsForZSet().add(Key.SINGLE, user.getIntraId() + Key.SINGLE , user.getPpp());
        redisTemplate.opsForZSet().add(Key.BUNGLE, user.getIntraId() + Key.BUNGLE, user.getPpp());
        RankFindDto rankFindDto = RankFindDto.builder().intraId(user.getIntraId()).gameType(GameType.valueOf(Key.SINGLE)).build();


        /*
        // when
        RankUserDto userInfoDto = rankService.findRank(rankFindDto);

        // then
        Assertions.assertThat(userInfoDto.getLosses()).isEqualTo(0);
        Assertions.assertThat(userInfoDto.getRanking()).isEqualTo(0);
        Assertions.assertThat(userInfoDto.getWinRate()).isEqualTo(0);
        Assertions.assertThat(userInfoDto.getPpp()).isEqualTo(0);
        Assertions.assertThat(userInfoDto.getWins()).isEqualTo(0);

         */
    }

    @Test
    @Transactional
    void findRankList() {
        // given
        /* page가 -값일 경우*/
        for (User user : users.values()) {
            RankRedis singleRank = RankRedis.from(UserDto.from(user), Key.SINGLE);
            RankRedis doubleRank = RankRedis.from(UserDto.from(user), Key.BUNGLE);
            redisTemplate.opsForValue().set(user.getIntraId() + Key.SINGLE, singleRank);
            redisTemplate.opsForValue().set(user.getIntraId() + Key.BUNGLE, doubleRank);
            redisTemplate.opsForZSet().add(Key.SINGLE, user.getIntraId() + Key.SINGLE, user.getPpp());
            redisTemplate.opsForZSet().add(Key.BUNGLE, user.getIntraId() + Key.BUNGLE, user.getPpp());
        }
        // when
        Pageable pageable = PageRequest.of(1,20);
        RankListDto rankList = rankRedisService.findRankList(RankFindListDto.builder().pageable(pageable).gameType(GameType.valueOf(Key.SINGLE)).count(10).build());

        // then
        Assertions.assertThat(rankList.getCurrentPage()).isEqualTo(0);
        Assertions.assertThat(rankList.getTotalPage()).isEqualTo(0);
        Assertions.assertThat(rankList.getRankList()).isNotNull();
        int ranking = 1;
        for (RankUserDto testUser : rankList.getRankList()){
            String intraId = testUser.getIntraId();
            Assertions.assertThat(users.get(intraId)).isNotNull();
            User hashMapUser = users.get(intraId);
            Assertions.assertThat(testUser.getPpp()).isEqualTo(hashMapUser.getPpp());
            Assertions.assertThat(testUser.getRank()).isEqualTo(ranking);
            Assertions.assertThat(testUser.getWinRate()).isNotNull();
            Assertions.assertThat(testUser.getStatusMessage()).isNotNull();
            ranking++;
        }
    }
}