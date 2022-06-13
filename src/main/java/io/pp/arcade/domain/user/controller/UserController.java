package io.pp.arcade.domain.user.controller;

import io.pp.arcade.domain.user.dto.UserDetailResponseDto;
import io.pp.arcade.domain.user.dto.UserHistoricResponseDto;
import io.pp.arcade.domain.user.dto.UserResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserController {
    UserResponseDto userFind(@RequestParam Integer userId);
    UserDetailResponseDto userFindDetail(@PathVariable String targetUserId, @RequestParam Integer currentUserId);
    UserHistoricResponseDto userFindHistorics(@PathVariable String userId, @PageableDefault(size = 10) Pageable pageable);
}
