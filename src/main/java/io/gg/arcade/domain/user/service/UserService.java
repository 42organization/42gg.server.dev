package io.gg.arcade.domain.user.service;

import io.gg.arcade.domain.user.dto.UserSaveRequestDto;
import io.gg.arcade.domain.user.entity.User;

public interface UserService {
    User addUser(UserSaveRequestDto userDto);
    void deleteUser();
    void updateUser();
    void getUserInfo();
}
