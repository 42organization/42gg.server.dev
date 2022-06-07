package io.gg.arcade.domain.user.service;

import io.gg.arcade.domain.user.dto.UserAddRequestDto;
import io.gg.arcade.domain.user.dto.UserModifyPppRequestDto;

public interface UserService {
    void UserModifyPpp(UserModifyPppRequestDto dto);
    void addUser(UserAddRequestDto dto);
}