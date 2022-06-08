package io.gg.arcade.domain.user.service;

import io.gg.arcade.domain.user.dto.UserAddRequestDto;
import io.gg.arcade.domain.user.dto.UserModifyPppRequestDto;
import io.gg.arcade.domain.user.entity.User;
import io.gg.arcade.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void userModifyPpp() {
        final Integer PPP = 1000;
        // given
        UserAddRequestDto addDto = UserAddRequestDto.builder()
                .intraId("donghyuk")
                .userImgUri("donghyuk's photo uri")
                .build();
        userService.addUser(addDto);

        User user = userRepository.findByIntraId("donghyuk").orElseThrow();
        UserModifyPppRequestDto modifyPppDto = UserModifyPppRequestDto.builder()
                .userId(user.getId())
                .ppp(PPP)
                .build();

        // when
        userService.UserModifyPpp(modifyPppDto);

        // then
        User modifiedUser = userRepository.findByIntraId("donghyuk").orElseThrow();
        Assertions.assertThat(PPP).isEqualTo(modifiedUser.getPpp());
    }

    @Test
    @Transactional
    void addUser() {
        //given
        UserAddRequestDto dto = UserAddRequestDto.builder()
                .intraId("hakim")
                .userImgUri("hakim's photo uri")
                .build();
        //when
        userService.addUser(dto);
        User user = User.builder()
                .intraId(dto.getIntraId())
                .userImgUri(dto.getUserImgUri())
                .racketType("pen")
                .statusMessage("")
                .isPlaying(false)
                .ppp(1000)
                .build();
        User dbUser = userRepository.findByIntraId("hakim").orElseThrow();
        //then
        Assertions.assertThat(user.getIntraId()).isEqualTo(dbUser.getIntraId());
        Assertions.assertThat(user.getUserImgUri()).isEqualTo(dbUser.getUserImgUri());
        Assertions.assertThat(user.getRacketType()).isEqualTo(dbUser.getRacketType());
        Assertions.assertThat(user.getStatusMessage()).isEqualTo(dbUser.getStatusMessage());
        Assertions.assertThat(user.getIsPlaying()).isEqualTo(dbUser.getIsPlaying());
        Assertions.assertThat(user.getPpp()).isEqualTo(dbUser.getPpp());
    }
}