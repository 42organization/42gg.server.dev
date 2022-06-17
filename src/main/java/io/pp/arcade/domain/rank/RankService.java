package io.pp.arcade.domain.rank;

import io.pp.arcade.domain.rank.dto.*;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.util.GameType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@AllArgsConstructor
public class RankService {
    private final UserRepository userRepository;
    private final RedisTemplate redisTemplate;
    private final RankRedisRepository rankRedisRepository;

    @Transactional
    public RankFindListDto findRankList(Pageable pageable, GameType type) {
        int pageNumber = pageable.getPageNumber() >= 0 ? pageable.getPageNumber() : 0;
        int pageSize = pageable.getPageSize();
        int userRanking = pageNumber * pageable.getPageSize();
        List<RankUserDto> rankList = new ArrayList<RankUserDto>();
        Set<String> range = redisTemplate.opsForZSet().reverseRange(type.getKey(), pageNumber * pageSize, (pageNumber + 1) * pageSize);
        for (String intraId : range){
            userRanking = userRanking + 1;
            RankRedis userInfo = (RankRedis) redisTemplate.opsForValue().get(intraId);
            rankList.add(RankUserDto.builder()
                        .intraId(userInfo.getIntraId())
                        .ppp(userInfo.getPpp())
                        .rank(userRanking)
                        .winRate(userInfo.getWinRate())
                        .build());
        }

        int currentPage = pageable.getPageNumber();
        int totalPage = redisTemplate.opsForZSet().size(type.getKey()).intValue() / pageSize;
        RankFindListDto findListDto =  RankFindListDto.builder()
                .currentPage(currentPage > totalPage ? totalPage : currentPage) // 최대값은 totalPage
                .totalPage(totalPage)
                .rankList(rankList)
                .build();
        return findListDto;
    }

    @Transactional
    public RankUserDto findRankById(RankFindDto findDto) {
        String key = getKey(findDto.getIntraId(), findDto.getGameType());
        RankRedis userRankInfo = (RankRedis) redisTemplate.opsForValue().get(key);
        Long userRanking = redisTemplate.opsForZSet().reverseRank(findDto.getGameType().getKey(), key);
        RankUserDto infoDto = RankUserDto.builder()
                .intraId(userRankInfo.getIntraId())
                .ppp(userRankInfo.getPpp())
                .winRate(userRankInfo.getWinRate())
                .rank(userRanking.intValue())
                .statusMessage(userRankInfo.getStatusMessage())
                .build();
        return infoDto;
    }

    @Transactional
    public void modifyRank(RankModifyDto modifyDto){
        String key = getKey(modifyDto.getIntraId(), modifyDto.getGameType());
        RankRedis rank = (RankRedis)redisTemplate.opsForValue().get(key);
        rank.update(modifyDto.getIsWin(),modifyDto.getPpp());
        redisTemplate.opsForValue().set(key, rank);
        redisTemplate.opsForZSet().add(modifyDto.getGameType().getKey(), key, modifyDto.getPpp());
    }

    @Transactional
    public void modifyRankStatusMessage(RankModifyStatusMessageDto modifyDto){
        String key = modifyDto.getIntraId() + modifyDto.getGameType();
        RankRedis rank = (RankRedis)redisTemplate.opsForValue().get(key);
        rank.setStatusMessage(modifyDto.getStatusMessage());
        redisTemplate.opsForValue().set(key, rank);
    }

    @Transactional
    public void addRank(RankAddDto addDto){
        User user = userRepository.findById(addDto.getUserId()).orElseThrow(()->new IllegalArgumentException("잘못된 요청입니다."));
        RankRedis singleRank =  RankRedis.from(user, GameType.SINGLE);
        RankRedis doubleRank =  RankRedis.from(user, GameType.DOUBLE);

        redisTemplate.opsForValue().set(user.getIntraId() + GameType.SINGLE, singleRank);
        redisTemplate.opsForValue().set(user.getIntraId() + GameType.DOUBLE, doubleRank);
        redisTemplate.opsForZSet().add(GameType.SINGLE.getKey(), user.getIntraId() + GameType.SINGLE , user.getPpp());
        redisTemplate.opsForZSet().add(GameType.DOUBLE.getKey(), user.getIntraId() + GameType.DOUBLE , user.getPpp());
    }

    @Transactional
    public void saveAllRankToRDB(RankSaveAllDto saveAllDto){
        //userRepository.saveAll();
    }
    private String getKey(String intraId, GameType GameType){
        return intraId + GameType.getKey();
    }
}
