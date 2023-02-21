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
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.GameType;
import lombok.AllArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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
    public UserSearchResponseAdminDto userAll(String keyword, Long page, HttpResponse httpResponse) {
        if (page < 1) {
            httpResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            return null;
        }
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
    public UserDetailResponseAdminDto userFindDetail(Integer userId) {
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
                .userId(targetUser.getId())
                .intraId(targetUser.getIntraId())
                .userImageUri(targetUser.getImageUri())
                .racketType(targetUser.getRacketType())
                .statusMessage(rankUserDto.getStatusMessage())
                .wins(rankUserDto.getWins())
                .losses(rankUserDto.getLosses())
                .ppp(rankUserDto.getPpp())
                .email(targetUser.getEMail())
                .roleType(targetUser.getRoleType().getKey())
                .build();
        return responseDto;
    } // domain에 의존함

    @Override
    @PutMapping(value = "/users/{userId}/detail")
    public ResponseEntity userDetailUpdate(UserUpdateRequestAdmintDto updateRequestDto, MultipartFile multipartFile) {
        try {
            if (multipartFile != null) {
                if (multipartFile.getSize() > 50000) {
                    return ResponseEntity.status(413).build();
                } else if (multipartFile.getContentType() == null || !multipartFile.getContentType().equals("image/jpeg")) {
                    return ResponseEntity.status(415).build();
                }
            }
            userAdminService.updateUserDetailByAdmin(updateRequestDto, multipartFile);
        } catch (IOException e) {
            throw new BusinessException("E0001");
        }
        return ResponseEntity.ok().build();
    }
    
    @Override
    @GetMapping(value = "/users/searches")
    public UserSearchResultAdminResponseDto userSearchResult(String inquiringString) {
        List<String> users = userAdminService.SearchUserByPartsOfIntraId(UserSearchAdminRequestDto.builder().intraId(inquiringString).build())
                .stream().map(UserAdminDto::getIntraId).collect(Collectors.toList());
        return UserSearchResultAdminResponseDto.builder().users(users).build();
    }
}
