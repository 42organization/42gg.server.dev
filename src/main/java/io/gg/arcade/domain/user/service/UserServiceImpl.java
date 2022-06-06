package io.gg.arcade.domain.user.service;

import io.gg.arcade.domain.user.dto.UserRequestDto;
import io.gg.arcade.domain.user.entity.User;
import io.gg.arcade.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public User addUser(UserRequestDto userDto) {
        User user = User.builder()
                .intraId(userDto.getIntraId())
                .userImageUri(userDto.getUserImageUri())
                .racketType(userDto.getRacketType())
                .isPlaying(false)
                .statusMessage("")
                .build();
        return userRepository.save(user);
    }

    @Override
    public void deleteUser() {

    }

    @Override
    public void updateUser() {

    }

    @Override
    public void getUserInfo() {
    }
}
