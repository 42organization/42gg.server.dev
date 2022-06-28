package io.pp.arcade.domain.rank.service;

import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.pp.arcade.TestInitiator;
import io.pp.arcade.domain.rank.Rank;
import io.pp.arcade.domain.rank.RankRepository;
import io.pp.arcade.domain.rank.dto.RankRedisDto;
import io.pp.arcade.domain.rank.dto.RankSaveAllDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.global.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class RankServiceTest {
    @Autowired
    private RankService rankService;
    @Autowired
    private RankRedisService rankRedisService;
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
    void flush() {
        flushAll();
    }

    @Test
    @Transactional
    @DisplayName("Redis -> DB - Redis 데이터 X")
    void saveAllWhenNoData() {
        /*
         * Redis - 데이터 없을 경우
         * */
        {
            // given
            List<RankRedisDto> rankRedisDtos = rankRedisService.findRankAll();
            RankSaveAllDto saveAllDto = RankSaveAllDto.builder().rankRedisDtos(rankRedisDtos).seasonId(1).build();

            // when
            rankService.saveAll(saveAllDto);

            // then
            Assertions.assertThat(rankRepository.findAll()).isEmpty();
        }




        /*
         * Redis - 데이터 있으며, SessionId가 있을 경우
         * */
        {
            List<RankRedisDto> rankRedisDtos = rankRedisService.findRankAll();
            RankSaveAllDto saveAllDto = RankSaveAllDto.builder().rankRedisDtos(rankRedisDtos).seasonId(null).build();

            // when

            // then
        }
    }

    @Test
    @Transactional
    @DisplayName("Redis -> DB - Redis 데이터 O, 시즌 정보 X")
    void saveAllWhenNoSeason() throws Exception{
        // season -> null

        // given
        testInitiator.letsgo();
        List<RankRedisDto> rankRedisDtos = rankRedisService.findRankAll();
        Map<String, RankRedisDto> rankRedisMap = new HashMap<String, RankRedisDto>();
        rankRedisDtos.forEach(rankRedisDto -> rankRedisMap.put(rankRedisDto.getIntraId(), rankRedisDto));
        RankSaveAllDto saveAllDto = RankSaveAllDto.builder().rankRedisDtos(rankRedisDtos).seasonId(null).build();

        // when
        assertThrows(BusinessException.class, () -> {
            rankService.saveAll(saveAllDto);
        });
    }

    @Test
    @Transactional
    @DisplayName("Redis -> DB - Redis 데이터 O, 시즌 정보 O")
    void saveAll() {
        // given
        testInitiator.letsgo();
        Integer seasonId = 1;
        List<RankRedisDto> rankRedisDtos = rankRedisService.findRankAll();
        Map<String, RankRedisDto> rankRedisMap = new HashMap<String, RankRedisDto>();
        rankRedisDtos.forEach(rankRedisDto -> rankRedisMap.put(rankRedisDto.getIntraId(), rankRedisDto));
        RankSaveAllDto saveAllDto = RankSaveAllDto.builder().rankRedisDtos(rankRedisDtos).seasonId(seasonId).build();

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
    void findAll() {
    }

    private void flushAll() {
        RedisClient redisClient = RedisClient.create("redis://"+ host + ":" + port);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        commands.flushall();
        boolean result = LettuceFutures.awaitAll(5, TimeUnit.SECONDS);
    }
}