package io.pp.arcade.domain.user;

import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserModifyPppDto;
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
        UserDto userDto = userService.findByIntraId("jiyun");

        //then
        Assertions.assertThat(userDto.getIntraId()).isEqualTo("jiyun");
    }

    @Test
    @Transactional
    void findById() {
        //given
        User user = userRepository.findByIntraId("jiyun").orElseThrow(() -> new IllegalArgumentException("haha"));

        //when
        UserDto userDto = userService.findById(user.getId());

        //then
        Assertions.assertThat(user.getId()).isEqualTo(userDto.getId());
    }

    @Test
    @Transactional
    void addUser() {
        //when
        userService.addUser("jiyun2");
        UserDto userDto = userService.findByIntraId("jiyun2");

        //then
        Assertions.assertThat(userDto.getIntraId()).isEqualTo("jiyun2");
    }

    @Test
    @Transactional
    void modifyUserPpp() {
        //given
        UserDto userDto = userService.findByIntraId("jiyun");
        UserModifyPppDto dto = UserModifyPppDto.builder()
                .userId(userDto.getId())
                .ppp(50)
                .build();

        //when
        userService.modifyUserPpp(dto);
        UserDto userDto2 = userService.findByIntraId("jiyun");

        //then
        Assertions.assertThat(userDto2.getPpp()).isEqualTo(50);
    }
}