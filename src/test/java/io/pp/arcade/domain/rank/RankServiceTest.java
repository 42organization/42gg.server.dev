package io.pp.arcade.domain.rank;

import io.pp.arcade.domain.rank.dto.*;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.util.RacketType;
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
    private RankService rankService;

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
        user = userRepository.save(User.builder().ppp(0).statusMessage("").intraId("donghyuk").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).build());
        user2 = userRepository.save(User.builder().ppp(100).statusMessage("").intraId("nheo").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).build());
        user3 = userRepository.save(User.builder().ppp(200).statusMessage("").intraId("hakim").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).build());
        user4 = userRepository.save(User.builder().ppp(300).statusMessage("").intraId("jiyun").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).build());
        user5 = userRepository.save(User.builder().ppp(400).statusMessage("").intraId("jekim").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).build());
        user6 = userRepository.save(User.builder().ppp(500).statusMessage("").intraId("wochae").eMail("").imageUri("").racketType(RacketType.SHAKEHAND).build());

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
        RankFindDto findUserDto =  RankFindDto.builder().intraId(user.getIntraId()).gameType("single").build();

        // when
        /*
        long start1 = System.currentTimeMillis();
        RankUserDto RankUserRankingDto = rankService.findRank(findUserDto);
        RankUserRankingDto.getRanking();
        long end1 = System.currentTimeMillis();

        // then
        System.out.println("getRanking - findById(slot): " + (end1 - start1));
        */
    }

    @Test
    @Transactional
    void addRank() {
        RankAddDto addDto = RankAddDto.builder().userId(user.getId()).ppp(0).build();
        RankModifyPppDto modifyPppDto = RankModifyPppDto.builder().intraId(user.getIntraId()).Ppp(100).gameType("single").build();
        // when
        rankService.addRank(addDto);
        rankService.modifyRankPpp(modifyPppDto);
        modifyPppDto = RankModifyPppDto.builder().intraId(user.getIntraId()).Ppp(200).gameType("single").build();
        rankService.modifyRankPpp(modifyPppDto);
        rankService.modifyRankStatusMessage(RankModifyStatusMessageDto.builder().statusMessage("ㅎㅎ").gameType("single").intraId(user.getIntraId()).build());
        //rankService.modifyRankStatusMessage(RankModifyStatusMessageDto.builder().intraId(user.getIntraId()).statusMessage("상태메시지 수정").gameType("single").build());
        // then
    }

    @Test
    void modifyRankPpp() {

    }

    @Test
    @Transactional
    void findRank() {
        // given
        RankRedis singleRank =  RankRedis.from(user,"single");
        RankRedis doubleRank =  RankRedis.from(user,"double");

        redisTemplate.opsForValue().set(user.getIntraId() + "single", singleRank);
        redisTemplate.opsForValue().set(user.getIntraId() + "double", doubleRank);
        redisTemplate.opsForZSet().add("rank", user.getIntraId() + "single" , user.getPpp());
        redisTemplate.opsForZSet().add("rank", user.getIntraId() + "double" , user.getPpp());
        RankFindDto rankFindDto = RankFindDto.builder().intraId(user.getIntraId()).gameType("single").build();


        // when
        /*
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
    void findRankList() {
        // given
        /* page가 -값일 경우*/
        for (User user : users.values()) {
            RankRedis singleRank = RankRedis.from(user, "single");
            RankRedis doubleRank = RankRedis.from(user, "double");

            redisTemplate.opsForValue().set(user.getIntraId() + "single", singleRank);
            redisTemplate.opsForValue().set(user.getIntraId() + "double", doubleRank);
            redisTemplate.opsForZSet().add("rank", user.getIntraId() + "single", user.getPpp());
            redisTemplate.opsForZSet().add("rank", user.getIntraId() + "double", user.getPpp());
        }
        // when
        Pageable pageable = PageRequest.of(1,20);
        RankFindListDto rankList = rankService.findRankList(pageable);

        // then
        /*
        Assertions.assertThat(rankList.getCurrentPage()).isEqualTo(0);
        Assertions.assertThat(rankList.getTotalPage()).isEqualTo(0);
        int ranking = 1;
        for (RankUserDto testUser : rankList.getRankList()){
            String intraId = testUser.getIntraId();
            Assertions.assertThat(users.get(intraId)).isNotNull();
            User HashMapUser = users.get(intraId);
            Assertions.assertThat(testUser.getWins()).isEqualTo(0);
            Assertions.assertThat(testUser.getLosses()).isEqualTo(0);
            Assertions.assertThat(testUser.getPpp()).isEqualTo(HashMapUser.getPpp());
            Assertions.assertThat(testUser.getWinRate()).isEqualTo(0);
            Assertions.assertThat(testUser.getRanking()).isEqualTo(ranking);
            ranking++;
        }
         */
    }
}