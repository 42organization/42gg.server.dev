package io.pp.arcade.v1.domain.user.controller;

import io.pp.arcade.v1.domain.user.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

public interface UserController {
    UserResponseDto userFind(HttpServletRequest request);
    UserDetailResponseDto userFindDetail(@PathVariable String targetUserId, HttpServletRequest request);
    UserRankResponseDto userFindRank(@PathVariable String targetUserId, @RequestParam Integer season, HttpServletRequest request);
    UserHistoricResponseDto userFindHistorics(@PathVariable String userId, @RequestParam Integer season, @PageableDefault(size = 10) Pageable pageable);
    void userModifyProfile(@RequestBody @Valid UserModifyProfileRequestDto userModifyProfileRequestDto, HttpServletRequest request);
    UserSearchResultResponseDto userSearchResult(@RequestParam(value="q", required = false, defaultValue = "") String inquiringString, HttpServletRequest request); // 유효성 검사 해야함
    UserLiveInfoResponseDto userLiveInfo(HttpServletRequest request) throws MessagingException;
}
