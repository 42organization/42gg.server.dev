package io.pp.arcade.domain.rank;

import io.pp.arcade.domain.rank.dto.*;
import io.pp.arcade.domain.season.SeasonRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.exception.BusinessException;
import io.pp.arcade.global.type.GameType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ListOperations;
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
    private final RankRepository rankRepository;
    private final RedisTemplate redisTemplate;
    private final SeasonRepository seasonRepository;

    @Transactional
    public RankFindListDto findRankList(Pageable pageable, GameType type) {
        int pageNumber = pageable.getPageNumber() >= 0 ? pageable.getPageNumber() : 0;
        int pageSize = pageable.getPageSize();
        Set<String> range = redisTemplate.opsForZSet().reverseRange(type.getKey(), pageNumber * pageSize, (pageNumber + 1) * pageSize);

        /* 랭킹리스트를 조회할 수 없을 경우 에러 반환*/
        if (range.isEmpty())
            throw new BusinessException("{server.internal.error}");

        List<RankUserDto> rankList = new ArrayList<RankUserDto>();
        for (String intraId : range){
            RankRedis userInfo = (RankRedis) redisTemplate.opsForValue().get(intraId);
            Integer totalGames = userInfo.getLosses() + userInfo.getWins();
            Integer rank = (totalGames == 0) ? -1 : redisTemplate.opsForZSet().reverseRank(type.getKey(),  intraId + type).intValue();
            rankList.add(RankUserDto.builder()
                        .intraId(intraId)
                        .ppp(userInfo.getPpp())
                        .rank(rank)
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
                .wins(userRankInfo.getWins())
                .losses(userRankInfo.getLosses())
                .winRate(userRankInfo.getWinRate())
                .rank(userRanking.intValue())
                .statusMessage(userRankInfo.getStatusMessage())
                .build();
        return infoDto;
    }

    @Transactional
    public void modifyUserPpp(RankModifyDto modifyDto){
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
        User user = userRepository.findById(addDto.getUserId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
        RankRedis singleRank =  RankRedis.from(user, GameType.SINGLE.getKey());
        RankRedis doubleRank =  RankRedis.from(user, GameType.BUNGLE.getKey());

        redisTemplate.opsForValue().set(user.getIntraId() + GameType.SINGLE, singleRank);
        redisTemplate.opsForValue().set(user.getIntraId() + GameType.BUNGLE, doubleRank);
        redisTemplate.opsForZSet().add(GameType.SINGLE.getKey(), user.getIntraId() + GameType.SINGLE , user.getPpp());
        redisTemplate.opsForZSet().add(GameType.BUNGLE.getKey(), user.getIntraId() + GameType.BUNGLE , user.getPpp());
    }

    @Transactional
    public void saveAllRankToRDB(RankSaveAllDto saveAllDto){
        List<Rank> ranks = rankRepository.findAllBySeasonId(saveAllDto.getSeasonId());
        ListOperations listOperations = redisTemplate.opsForList();
        // 랭크 테이블에 해당 시즌의 정보가 있을 경우 유저정보 업데이트
        // 랭크 테이블에 해당 시즌의 정보가 없을 경우 새로운 유저 생성

        rankRepository.saveAll(ranks);
    }

    @Transactional
    public void loadAllRankFromRDB(RankSaveAllDto saveAllDto){
        //userRepository.saveAll();
    }


    private String getKey(String intraId, GameType GameType){
        return intraId + GameType.getKey();
    }
}
