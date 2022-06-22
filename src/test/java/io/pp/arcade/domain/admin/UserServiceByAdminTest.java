package io.pp.arcade.domain.admin;

import io.pp.arcade.domain.admin.dto.create.UserCreateRequestDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class UserServiceByAdminTest {
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

        userRepository.save(User.builder()
                .intraId("donghyuk")
                .statusMessage("")
                .ppp(1)
                .build()
        );

        userRepository.save(User.builder()
                .intraId("nheo")
                .statusMessage("")
                .ppp(1)
                .build()
        );

        userRepository.save(User.builder()
                .intraId("hakim")
                .statusMessage("")
                .ppp(1)
                .build()
        );

        userRepository.save(User.builder()
                .intraId("jekim")
                .statusMessage("")
                .ppp(1)
                .build()
        );

        userRepository.save(User.builder()
                .intraId("woche")
                .statusMessage("")
                .ppp(1)
                .build()
        );
    }

    @Test
    @Transactional
    void findByAdmin() {
        //given
        Pageable pageable = PageRequest.of(0, 5);

        //when
        List<UserDto> users = userService.findUserByAdmin(pageable);

        //then
        Assertions.assertThat(users.get(0).getIntraId()).isEqualTo("jiyun");
        Assertions.assertThat(users.size()).isEqualTo(5);
    }
}
