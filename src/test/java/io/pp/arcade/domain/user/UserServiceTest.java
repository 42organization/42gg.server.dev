package io.pp.arcade.domain.user;

import io.pp.arcade.domain.user.dto.*;
import io.pp.arcade.global.util.RacketType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository.save(User.builder()
                .intraId("jiyun")
                .statusMessage("")
                .ppp(1)
                .build()
        );
    }

    @Test
    @Transactional
    void findByIntraId() {
        //when
        UserDto userDto = userService.findByIntraId(UserFindDto.builder().intraId("jiyun").build());

        //then
        Assertions.assertThat(userDto.getIntraId()).isEqualTo("jiyun");
    }

    @Test
    @Transactional
    void findById() {
        //given
        User user = userRepository.findByIntraId("jiyun").orElseThrow(() -> new IllegalArgumentException("haha"));

        //when
        UserDto userDto = userService.findById(UserFindDto.builder().userId(user.getId()).build());

        //then
        Assertions.assertThat(userDto.getIntraId()).isEqualTo(userDto.getIntraId());
    }

    @Test
    @Transactional
    void addUser() {
        //when
        userService.addUser(UserAddDto.builder().intraId("jiyun2").build());
        UserDto userDto = userService.findByIntraId(UserFindDto.builder().intraId("jiyun2").build());

        //then
        Assertions.assertThat(userDto.getIntraId()).isEqualTo("jiyun2");
    }

    @Test
    @Transactional
    void modifyUserPpp() {
        //given

        UserDto userDto = userService.findByIntraId(UserFindDto.builder().intraId("jiyun").build());
        UserModifyPppDto dto = UserModifyPppDto.builder()
                .userId(userDto.getId())
                .ppp(50)
                .build();

        //when
        userService.modifyUserPpp(dto);
        UserDto userDto2 = userService.findByIntraId(UserFindDto.builder().intraId("jiyun").build());

        //then
        Assertions.assertThat(userDto2.getPpp()).isEqualTo(50);
    }

    @Test
    @Transactional
    void modifyUserProfile() {
        //given
        UserDto userDto = userService.findByIntraId(UserFindDto.builder().intraId("jiyun").build());
        UserModifyProfileDto dto = UserModifyProfileDto.builder()
                .userId(userDto.getId())
                .userImageUri("image")
                .racketType(RacketType.SHAKEHAND)
                .statusMessage("선출 아님").build();
        //when
        userService.modifyUserProfile(dto);
        User user = userRepository.findByIntraId("jiyun").orElseThrow();

        //then
        Assertions.assertThat(user.getImageUri()).isEqualTo("image");
        Assertions.assertThat(user.getRacketType()).isEqualTo(RacketType.SHAKEHAND);
        Assertions.assertThat(user.getStatusMessage()).isEqualTo("선출 아님");
    }
}