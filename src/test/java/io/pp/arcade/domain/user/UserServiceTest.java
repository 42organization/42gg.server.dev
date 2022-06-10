package io.pp.arcade.domain.user;

import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserModifyPppRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

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
        UserDto userDto = userService.findByIntraId("jiyun");
        Assertions.assertThat(userDto.getIntraId()).isEqualTo("jiyun");
        System.out.println(userDto.getId());
    }

    @Test
    @Transactional
    void findById() {
        User user = userRepository.findByIntraId("jiyun").orElseThrow(() -> new IllegalArgumentException("haha"));
        UserDto userDto = userService.findById(user.getId());
        Assertions.assertThat(user.getId()).isEqualTo(userDto.getId());
        System.out.println(userDto.getId());
    }

    @Test
    @Transactional
    void addUser() {
        userService.addUser("jiyun2");
        UserDto userDto = userService.findByIntraId("jiyun2");
        Assertions.assertThat(userDto.getIntraId()).isEqualTo("jiyun2");
        System.out.println(userDto.getId());
    }

    @Test
    @Transactional
    void modifyUserPpp() {
        UserDto userDto = userService.findByIntraId("jiyun");
        UserModifyPppRequestDto dto = UserModifyPppRequestDto.builder()
                .userId(userDto.getId())
                .ppp(50)
                .build();
        userService.modifyUserPpp(dto);
        UserDto userDto2 = userService.findByIntraId("jiyun");
        Assertions.assertThat(userDto2.getPpp()).isEqualTo(50);
        System.out.println(userDto.getId());

    }
}