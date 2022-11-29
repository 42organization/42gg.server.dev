package io.pp.arcade.domain.rank.service;

import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.rank.RankRepository;
import io.pp.arcade.v1.domain.rank.service.RankService;
import io.pp.arcade.v1.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RankServiceTest {
    @Autowired
    private RankService rankService;
    @Autowired
    private RankRepository rankRepository;
    @Autowired
    private TestInitiator testInitiator;
    @Value("${spring.redis.host}")
    String host;
    @Value("${spring.redis.port}")
    String port;
    private List<User> users;

    @BeforeEach
    void init (){
        flushAll();
    }
/*
    @Test
    @Transactional
    @DisplayName("랭크 저장 Redis -> DB - Redis 데이터 X")
    void saveAllWhenNoData() {
        // given
        List<RankRedisDto> rankRedisDtos = rankRedisService.findRankAll();
        RankSaveAllDto saveAllDto = RankSaveAllDto.builder().rankUserDtos(rankRedisDtos).seasonId(1).build();

        // when
        rankService.saveAll(saveAllDto);

        // then
        Assertions.assertThat(rankRepository.findAll()).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("랭크 저장 Redis -> DB - Redis 데이터 O, 시즌 정보 X")
    void saveAllWhenNoSeason() throws Exception{
        // given
        testInitiator.letsgo();
        List<RankRedisDto> rankRedisDtos = rankRedisService.findRankAll();
        Map<String, RankRedisDto> rankRedisMap = new HashMap<String, RankRedisDto>();
        rankRedisDtos.forEach(rankRedisDto -> rankRedisMap.put(rankRedisDto.getIntraId(), rankRedisDto));
        RankSaveAllDto saveAllDto = RankSaveAllDto.builder().rankUserDtos(rankRedisDtos).seasonId(null).build();

        // when
        assertThrows(BusinessException.class, () -> {
            rankService.saveAll(saveAllDto);
        });
    }

    @Test
    @Transactional
    @DisplayName("랭크 저장 Redis -> DB - Redis 데이터 O, 시즌 정보 O")
    void saveAll() {
        // given
        testInitiator.letsgo();
        Integer seasonId = 1;
        List<RankRedisDto> rankRedisDtos = rankRedisService.findRankAll();
        Map<String, RankRedisDto> rankRedisMap = new HashMap<String, RankRedisDto>();
        rankRedisDtos.forEach(rankRedisDto -> rankRedisMap.put(rankRedisDto.getIntraId(), rankRedisDto));
        RankSaveAllDto saveAllDto = RankSaveAllDto.builder().rankUserDtos(rankRedisDtos).seasonId(seasonId).build();

        // when
        rankService.saveAll(saveAllDto);

        // then
        List<Rank> rankList = rankRepository.findAll();
        Assertions.assertThat(rankList).isNotEmpty();
        for (Rank rank : rankList) {
            RankRedisDto rankRedisDto = rankRedisMap.get(rank.getUser().getIntraId());
            Assertions.assertThat(rank.getUser().getIntraId()).isEqualTo(rankRedisDto.getIntraId());
            Assertions.assertThat(rank.getSeasonId()).isEqualTo(seasonId);
            Assertions.assertThat(rank.getRanking()).isEqualTo(rankRedisDto.getRanking());
            Assertions.assertThat(rank.getWins()).isEqualTo(rankRedisDto.getWins());
            Assertions.assertThat(rank.getLosses()).isEqualTo(rankRedisDto.getLosses());
            Assertions.assertThat(rank.getRacketType()).isEqualTo(rankRedisDto.getRacketType());
            Assertions.assertThat(rank.getUser().getPpp()).isEqualTo(rankRedisDto.getPpp());
        }
    }

    @Test
    @Transactional
    @DisplayName("모든 랭크 조회")
    void findAll() {
        // given
        testInitiator.letsgo();
        users = Arrays.asList(testInitiator.users);
        List<Rank> rankList = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            Rank rank = Rank.builder().ppp(1000).seasonId(1).ranking(i).wins(i).losses(i).gameType(GameType.SINGLE).racketType(RacketType.SHAKEHAND).user(users.get(i)).build();
            rankList.add(rank);
        }
        //List<RankDto> rankDtos = rankList.stream().map(RankDto::from).collect(Collectors.toList());
        rankRepository.saveAll(rankList);

        // when
        List<RankDto> rankDtos = rankService.findAll();

        // then
        Assertions.assertThat(rankDtos).isNotEmpty();
        Assertions.assertThat(rankDtos.size()).isEqualTo(rankList.size());
    }

    @Test
    @Transactional
    @DisplayName("모든 랭크 조회 - 데이터가 없을 경우")
    void findAllWhenNoData() {
        // when
        List<RankDto> rankDtos = rankService.findAll();
        // then
        Assertions.assertThat(rankDtos).isEmpty();
    }
 */

    private void flushAll() {
        RedisClient redisClient = RedisClient.create("redis://"+ host + ":" + port);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        commands.flushall();
        LettuceFutures.awaitAll(5, TimeUnit.SECONDS);
    }

}