package io.pp.arcade.domain.rank;

import io.pp.arcade.domain.admin.dto.create.RankCreateRequestDto;
import io.pp.arcade.domain.admin.dto.delete.RankDeleteDto;
import io.pp.arcade.domain.admin.dto.update.RankUpdateRequestDto;
import io.pp.arcade.domain.rank.dto.*;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class RankService {
    private final String CACHE_VALUE = "rank";
    private final RankRepository rankRepository;
    private final UserRepository userRepository;
    private final RedisTemplate redisTemplate;
    private final RankRedisRepository rankRedisRepository;

    @Transactional
    public RankFindListDto findRankList(Pageable pageable) {
        // pageable.size() 반환 MAX값 필터링
        int pageNumber = pageable.getPageNumber() >= 0 ? pageable.getPageNumber() : 0;
        int pageSize = pageable.getPageSize();
        int rankingIdx = pageNumber * pageable.getPageSize();
        List<RankUserDto> rankList = new ArrayList<RankUserDto>();
        Set<String> range = redisTemplate.opsForZSet().range(CACHE_VALUE, pageNumber * pageSize, pageNumber * pageSize * 2);

        for (String key : range){
            RankRedis userInfo = (RankRedis) redisTemplate.opsForValue().get(key);
            rankList.add(RankUserDto.builder()
                        .intraId(userInfo.getIntraId())
                        .ppp(userInfo.getPpp())
                        .rank(rankingIdx)
                        .winRate(userInfo.getWinRate())
                        .build());
            rankingIdx++;
        }

        int currentPage = pageable.getPageNumber();
        int totalPage = redisTemplate.opsForZSet().size(CACHE_VALUE).intValue() / pageSize;
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
        Long userRanking = redisTemplate.opsForZSet().reverseRank(CACHE_VALUE, key);

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
    public void modifyRankPpp(RankModifyPppDto modifyDto){
        String key = getKey(modifyDto.getIntraId(), modifyDto.getGameType());
        RankRedis rank = (RankRedis)redisTemplate.opsForValue().get(key);
        rank.setPpp(modifyDto.getPpp());
        redisTemplate.opsForValue().set(key, rank);
        redisTemplate.opsForZSet().add(CACHE_VALUE, key, modifyDto.getPpp());

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
        RankRedis singleRank =  RankRedis.from(user,"single");
        RankRedis doubleRank =  RankRedis.from(user,"double");

        redisTemplate.opsForValue().set(user.getIntraId() + "single", singleRank);
        redisTemplate.opsForValue().set(user.getIntraId() + "double", doubleRank);
        redisTemplate.opsForZSet().add(CACHE_VALUE, user.getIntraId() + "single" , user.getPpp());
        redisTemplate.opsForZSet().add(CACHE_VALUE, user.getIntraId() + "double" , user.getPpp());
    }
    // MyRank 조회
    // 유저 statusMessage 업데이트

    private String getKey(String intraId, String GameType){
        return intraId + GameType;
    }

    @Transactional
    public void createRankByAdmin(RankCreateRequestDto createRequestDto) {
        User user = userRepository.findById(createRequestDto.getUserId()).orElseThrow();
        Rank rank = Rank.builder()
                .user(user)
                .seasonId(createRequestDto.getSeasonId())
                .racketType(createRequestDto.getRacketType())
                .ppp(createRequestDto.getPpp())
                .ranking(createRequestDto.getRangking())
                .wins(createRequestDto.getWins())
                .losses(createRequestDto.getLosses())
                .build();
        rankRepository.save(rank);
    }

    @Transactional
    public void updateRankByAdmin(RankUpdateRequestDto updateRequestDto) {
        Rank rank = rankRepository.findById(updateRequestDto.getRankId()).orElseThrow();
        rank.setPpp(updateRequestDto.getPpp());
    }

    @Transactional
    public void deleteRankByAdmin(RankDeleteDto deleteDto) {
        Rank rank = rankRepository.findById(deleteDto.getRankId()).orElseThrow();
        rankRepository.delete(rank);
    }

    @Transactional
    public List<RankDto> findRankByAdmin(Pageable pageable) {
        Page<Rank> ranks = rankRepository.findAll(pageable);
        List<RankDto> rankDtos = ranks.stream().map(RankDto::from).collect(Collectors.toList());
        return rankDtos;
    }
}
