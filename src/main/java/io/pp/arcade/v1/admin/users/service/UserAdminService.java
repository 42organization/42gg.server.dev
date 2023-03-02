package io.pp.arcade.v1.admin.users.service;

import io.pp.arcade.v1.admin.users.dto.*;
import io.pp.arcade.v1.admin.users.repository.UserAdminRepository;
import io.pp.arcade.v1.domain.rank.RankRedisRepository;
import io.pp.arcade.v1.domain.rank.RedisKeyManager;
import io.pp.arcade.v1.domain.rank.dto.RankKeyGetDto;
import io.pp.arcade.v1.domain.rank.dto.RedisRankUpdateDto;
import io.pp.arcade.v1.domain.rank.dto.RedisRankingUpdateDto;
import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.SeasonRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.exception.RankUpdateException;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.RoleType;
import io.pp.arcade.v1.global.util.AsyncNewUserImageUploader;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserAdminService {
    private final UserAdminRepository userAdminRepository;

    /* 도메인에 의존함 */
    private final RankRedisRepository rankRedisRepository;
    private final SeasonRepository seasonRepository;
    private final RedisKeyManager redisKeyManager;
    private final AsyncNewUserImageUploader asyncNewUserImageUploader;

    @Transactional
    public List<UserAdminDto> findAll() {
        List<User> users = userAdminRepository.findAll();
        List<UserAdminDto> userAdminDtos = users.stream().map(UserAdminDto::from).collect(Collectors.toList());
        return userAdminDtos;
    }
    @Transactional
    public UserAdminDto findByIntraId(UserFindAdminDto findDto) {
        User user = userAdminRepository.findByIntraId(findDto.getIntraId()).orElseThrow(() -> new BusinessException("E0001"));
        return UserAdminDto.from(user);
    }

    @Transactional
    public UserAdminDto findById(UserFindAdminDto findDto) {
        User user = userAdminRepository.findById(findDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));
        return UserAdminDto.from(user);
    }

    /* 문자열을 포함하는 intraId를 가진 유저 찾기 */
    @Transactional
    public UserSearchResponseAdminDto findByPartsOfIntraId(UserSearchRequestAdminDto userSearchDto, Pageable pageable) {
        Page<UserSearchAdminDto> result;
        Page<User> users = userAdminRepository.findByIntraIdContains(userSearchDto.getIntraId(), pageable);
        result = users.map(UserSearchAdminDto::from);
        return UserSearchResponseAdminDto.builder()
                .userSearchAdminDtos(result.getContent())
                .totalPage(result.getTotalPages())
                .currentPage(result.getNumber() + 1)
                .build();
    }

    @Transactional
    public List<UserAdminDto> SearchUserByPartsOfIntraId(UserSearchAdminRequestDto userSearchAdminRequestDto) {
        List<UserAdminDto> result = new ArrayList<UserAdminDto>();
        if (!userSearchAdminRequestDto.getIntraId().isEmpty()){
            Pageable pageable = PageRequest.of(0, 5, JpaSort.unsafe("locate('" + userSearchAdminRequestDto.getIntraId() + "', intraId)").ascending().and(Sort.by("intraId")));
            Page<User> users = userAdminRepository.findByIntraIdContains(userSearchAdminRequestDto.getIntraId(), pageable);
            result.addAll(users.stream().map(UserAdminDto::from).collect(Collectors.toList()));
        }
        return result;
    }


    @Transactional
    public UserSearchResponseAdminDto findUserByAdmin(Pageable pageable) {
        Page<User> users = userAdminRepository.findAll(pageable);
        Page<UserSearchAdminDto> page = users.map(UserSearchAdminDto::from);
        return UserSearchResponseAdminDto.builder()
                .userSearchAdminDtos(page.getContent())
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber() + 1)
                .build();
    }

    @Transactional( noRollbackFor = { RankUpdateException.class })
    public Boolean updateUserDetailByAdmin(UserUpdateRequestAdmintDto updateRequestDto, MultipartFile multipartFile) throws IOException {
        User user = userAdminRepository.findById(updateRequestDto.getUserId()).orElseThrow();
        user.setEMail(updateRequestDto.getEmail());
        user.setRacketType(updateRequestDto.getRacketType());
        user.setStatusMessage(updateRequestDto.getStatusMessage());
        user.setRoleType(RoleType.of(updateRequestDto.getRoleType()));
        userAdminRepository.save(user);

        if (multipartFile != null) {
            asyncNewUserImageUploader.update(updateRequestDto.getIntraId(), multipartFile);
        }

        RankRedis rankRedis = rankRedisRepository.findRank(redisKeyManager.getCurrentRankKey(), user.getId());
        if (rankRedis == null) {
            throw new RankUpdateException("RK001");
        }

        rankRedis.setPpp(updateRequestDto.getPpp());
        rankRedis.setWins(updateRequestDto.getWins());
        rankRedis.setLosses(updateRequestDto.getLosses());
        rankRedis.setStatusMessage(updateRequestDto.getStatusMessage());
        rankRedis.setRacketType(updateRequestDto.getRacketType());
        Integer wins = updateRequestDto.getWins();
        Integer losses = updateRequestDto.getLosses();
        rankRedis.setWinRate((wins + losses) == 0 ? 0 : (double)(wins * 10000 / (wins + losses)) / 100);

        Season season = seasonRepository.findFirstByModeOrModeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(Mode.BOTH, Mode.RANK, LocalDateTime.now()).orElseThrow(() -> new BusinessException("E0001"));
        String redisSeason = redisKeyManager.getSeasonKey(season.getId().toString(), season.getSeasonName());
        RedisRankUpdateDto redisRankUpdateDto = RedisRankUpdateDto.builder()
                .userRank(rankRedis)
                .userId(rankRedis.getId())
                .seasonKey(redisSeason)
                .build();
        rankRedisRepository.updateRank(redisRankUpdateDto);

        RankKeyGetDto rankKeyGetDto = RankKeyGetDto.builder().seasonId(season.getId()).seasonName(season.getSeasonName()).build();
        String curRankingKey = redisKeyManager.getRankingKeyBySeason(rankKeyGetDto, GameType.SINGLE);
        RedisRankingUpdateDto redisRankingUpdateDto = RedisRankingUpdateDto.builder()
                .rankingKey(curRankingKey)
                .rank(rankRedis)
                .ppp(rankRedis.getPpp())
                .build();
        rankRedisRepository.updateRanking(redisRankingUpdateDto);
        return true;
    }
}
