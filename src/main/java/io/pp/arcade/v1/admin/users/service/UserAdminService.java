package io.pp.arcade.v1.admin.users.service;

import io.pp.arcade.v1.admin.users.dto.*;
import io.pp.arcade.v1.admin.users.repository.UserAdminRepository;
import io.pp.arcade.v1.domain.rank.RankRedisRepository;
import io.pp.arcade.v1.domain.rank.RankRepository;
import io.pp.arcade.v1.domain.rank.RedisKeyManager;
import io.pp.arcade.v1.domain.rank.dto.RankRedisFindDto;
import io.pp.arcade.v1.domain.rank.dto.RankUserDto;
import io.pp.arcade.v1.domain.rank.dto.RedisRankUpdateDto;
import io.pp.arcade.v1.domain.rank.entity.RankRedis;
import io.pp.arcade.v1.domain.rank.service.RankRedisService;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.SeasonRepository;
import io.pp.arcade.v1.domain.season.SeasonService;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserService;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.domain.user.dto.UserFindDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.util.AsyncNewUserImageUploader;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public UserSearchResponseAdminDto findUserByAdmin(Pageable pageable) {
        Page<User> users = userAdminRepository.findAll(pageable);
        Page<UserSearchAdminDto> page = users.map(UserSearchAdminDto::from);
        return UserSearchResponseAdminDto.builder()
                .userSearchAdminDtos(page.getContent())
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber() + 1)
                .build();
    }

//    @Transactional
//    public void updateUserDetailByAdmin(UserUpdateRequesAdmintDto updateRequestDto) {
//        User user = userAdminRepository.findById(updateRequestDto.getUserId()).orElseThrow();
//        user.setEMail(updateRequestDto.getEmail());
//        user.setRacketType(updateRequestDto.getRacketType());
//        user.setStatusMessage(updateRequestDto.getStatusMessage());
//        user.setRoleType(updateRequestDto.getRoleType());
//
//        RankRedis rankRedis = rankRedisRepository.findRank(redisKeyManager.getCurrentRankKey(), updateRequestDto.getUserId());
//        rankRedis.setWins(updateRequestDto.getWins());
//        rankRedis.setLosses(updateRequestDto.getLosses());
//        rankRedis.setRacketType(updateRequestDto.getRacketType());
//        Integer wins = updateRequestDto.getWins();
//        Integer losses = updateRequestDto.getLosses();
//        rankRedis.setWinRate((wins + losses) == 0 ? 0 : (double)(wins * 10000 / (wins + losses)) / 100);
//
//        Season season = seasonRepository.findFirstBySeasonModeOrSeasonModeOrderByIdDesc(Mode.RANK, Mode.BOTH).orElseThrow(() -> new BusinessException("E0001"));
//        RedisRankUpdateDto redisRankUpdateDto = RedisRankUpdateDto.builder()
//                .userRank(rankRedis)
//                .userId(updateRequestDto.getUserId())
//                .seasonKey(season.getSeasonName())
//                .build();
//        rankRedisRepository.updateRank(redisRankUpdateDto);
//
////        MultipartFile multiPartFile = updateRequestDto.getImgFile();
////        if (multiPartFile != null)
////            asyncNewUserImageUploader.update(user.getIntraId(), multiPartFile);
//    }
}
