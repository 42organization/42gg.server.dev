package io.gg.arcade.domain.user.service;

import io.gg.arcade.domain.user.dto.UserRequestDto;
import io.gg.arcade.domain.user.entity.User;

public interface UserService {
    User addUser(UserRequestDto userDto);
    void deleteUser();
    void updateUser();
    void getUserInfo();
}
