package io.pp.arcade.v1.admin.users.controller;

import io.pp.arcade.v1.admin.users.dto.*;
import io.pp.arcade.v1.admin.users.service.UserAdminService;
import io.pp.arcade.v1.domain.rank.dto.RankRedisFindDto;
import io.pp.arcade.v1.domain.rank.dto.RankUserDto;
import io.pp.arcade.v1.domain.rank.service.RankRedisService;
import io.pp.arcade.v1.domain.season.SeasonService;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.user.UserService;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.domain.user.dto.UserFindDto;
import io.pp.arcade.v1.global.type.GameType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(value = "pingpong/admin")
public class UserAdminControllerImpl implements UserAdminController {
    private final UserAdminService userAdminService;

    /* domain 의존 서비스 */
    private final UserService userService;
    private final SeasonService seasonService;
    private final RankRedisService rankRedisService;

    @Override
    @GetMapping(value = "/users")
    public UserSearchResponseAdminDto userAll(HttpServletRequest request, String keyword, Long page) {
        Pageable pageable = PageRequest.of(page.intValue() - 1, 20);

        if (keyword == null) {
            UserSearchResponseAdminDto users = userAdminService.findUserByAdmin(pageable);
            return users;
        }
        UserSearchRequestAdminDto userSearchDto = UserSearchRequestAdminDto.builder()
                                                    .intraId(keyword)
                                                    .build();
        UserSearchResponseAdminDto users = userAdminService.findByPartsOfIntraId(userSearchDto, pageable);
        return users;
    }

    @Override
    @GetMapping(value = "/users/{userId}/detail")
    public UserDetailResponseAdminDto userFindDetail(Integer userId, HttpServletRequest request) {
        UserDto targetUser = userService.findById(UserFindDto.builder()
                .userId(userId)
                .build());
        SeasonDto seasonDto = seasonService.findLatestRankSeason();
        RankRedisFindDto rankRedisFindDto = RankRedisFindDto.builder()
                .user(targetUser)
                .gameType(GameType.getEnumFromValue("SINGLE")) // 현재는 싱글로만 동작
                .season(seasonDto)
                .build();
        RankUserDto rankUserDto = rankRedisService.findRankById(rankRedisFindDto);
        UserDetailResponseAdminDto responseDto = UserDetailResponseAdminDto.builder()
                .intraId(targetUser.getIntraId())
                .userImageUri(targetUser.getImageUri())
                .racketType(targetUser.getRacketType())
                .statusMessage(rankUserDto.getStatusMessage())
                .wins(rankUserDto.getWins())
                .losses(rankUserDto.getLosses())
                .ppp(rankUserDto.getPpp())
                .eMail(targetUser.getEMail())
                .roleType(targetUser.getRoleType().getDisplayName())
                .build();
        return responseDto;
    } // domain에 의존함

    @Override
    @PutMapping(value = "/users/{userId}/detail")
    public void userDetailUpdate(UserUpdateRequestAdmintDto updateRequestDto, MultipartFile multipartFile, HttpServletRequest request) {
        userAdminService.updateUserDetailByAdmin(updateRequestDto, multipartFile);
    }
    
    @Override
    @GetMapping(value = "/users/searches")
    public UserSearchResultAdminResponseDto userSearchResult(String inquiringString/*HttpServletRequest request*/) {
        List<String> users = userAdminService.SearchUserByPartsOfIntraId(UserSearchAdminRequestDto.builder().intraId(inquiringString).build())
                .stream().map(UserAdminDto::getIntraId).collect(Collectors.toList());
        return UserSearchResultAdminResponseDto.builder().users(users).build();
    }
}
