package io.gg.arcade.domain.user.service;

import io.gg.arcade.domain.user.dto.UserRequestDto;
import io.gg.arcade.domain.user.entity.User;
import io.gg.arcade.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("유저 생성")
    void addUser() {
        //given
        UserRequestDto userDto = new UserRequestDto();
        userDto.setIntraId("hakim");
        userDto.setUserImageUri("");
        userDto.setRacketType("pen");
        userDto.setStatusMessage("Hello pingpong!");

        //when
        User user1 = userService.addUser(userDto);
        User user2 = userRepository.getById(user1.getId());

        //then
        Assertions.assertThat(user1).isEqualTo(user2);
    }

    @Test
    void deleteUser() {
        //given

        //when

        //then
        userService.deleteUser();

    }

    @Test
    void updateUser() {
        //given

        //when

        //then
         userService.updateUser();
    }

    @Test
    void getUserInfo() {
        //given

        //when

        //then
        userService.getUserInfo();
    }
}