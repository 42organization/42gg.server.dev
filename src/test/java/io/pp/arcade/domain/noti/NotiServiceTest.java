package io.pp.arcade.domain.noti;

import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.noti.dto.*;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.type.GameType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotiServiceTest {

    @Autowired
    NotiRepository notiRepository;

    @Autowired
    NotiService notiService;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    SlotRepository slotRepository;

    Slot slot;
    Team team1;
    User user1;
    User user2;
    Team team2;
    User user3;
    User user4;

    @BeforeEach
    void init() {
        user1 = userRepository.save(User.builder().intraId("jiyun1").eMail("kipark@student.42seoul.kr").statusMessage("").ppp(42).build());
        user2 = userRepository.save(User.builder().intraId("jiyun2").eMail("kipark@student.42seoul.kr").statusMessage("").ppp(24).build());
        user3 = userRepository.save(User.builder().intraId("nheo1").eMail("kipark@student.42seoul.kr").statusMessage("").ppp(60).build());
        user4 = userRepository.save(User.builder().intraId("nheo2").eMail("kipark@student.42seoul.kr").statusMessage("").ppp(30).build());
        team1 = teamRepository.save(Team.builder()
                .teamPpp(0)
                .user1(user1)
                .user2(user2)
                .headCount(2)
                .score(0)
                .build());
        team2 = teamRepository.save(Team.builder()
                .teamPpp(0)
                .user1(user3)
                .user2(user4)
                .headCount(2)
                .score(0)
                .build());
        slot = slotRepository.save(Slot.builder()
                .tableId(1)
                .team1(team1)
                .team2(team2)
                .type(GameType.DOUBLE)
                .time(LocalDateTime.now())
                .headCount(4)
                .build());
    }


    @Test
    @Transactional
    void addNoti() throws MessagingException {
        NotiAddDto addDto = NotiAddDto.builder()
                .notiType("announce")
                .user(UserDto.from(user1))
                .message("hi").build();
        notiService.addNoti(addDto);

        Assertions.assertThat(notiRepository.findAll()).isNotEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    @Transactional
    void findNotiByUser() {
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("matched")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("canceled")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("matched")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("imminent")
                .isChecked(false)
                .slot(slot)
                .build());

        NotiFindDto notiFindDto = NotiFindDto.builder()
                .user(UserDto.from(user1))
                .build();
        List<NotiDto> notiDtoList = notiService.findNotiByUser(notiFindDto);

        Assertions.assertThat(notiDtoList.size()).isEqualTo(4);
    }

    @Test
    @Transactional
    void countAllNByUser() {
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("matched")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("canceled")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("matched")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("imminent")
                .isChecked(false)
                .slot(slot)
                .build());

        NotiFindDto notiFindDto = NotiFindDto.builder()
                .user(UserDto.from(user1))
                .build();
        NotiCountDto notiCount = notiService.countAllNByUser(notiFindDto);

        Assertions.assertThat(notiCount.getNotiCount()).isEqualTo(4);
    }

    @Test
    @Transactional
    void modifyNotiChecked() {
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("matched")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("canceled")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("matched")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("imminent")
                .isChecked(false)
                .slot(slot)
                .build());

        NotiModifyDto modifyDto = NotiModifyDto.builder()
                .user(UserDto.from(user1))
                .build();

        notiService.modifyNotiChecked(modifyDto);
        Assertions.assertThat(notiRepository.findAll().get(0).getIsChecked()).isEqualTo(true);
    }

    @Test
    @Transactional
    void removeAllNotisByUser() {
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("matched")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("canceled")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("matched")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("imminent")
                .isChecked(false)
                .slot(slot)
                .build());

        NotiDeleteDto deleteDto = NotiDeleteDto.builder()
                .user(UserDto.from(user1))
                .build();
        notiService.removeAllNotisByUser(deleteDto);
        Assertions.assertThat(notiRepository.findAll()).isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    @Transactional
    void removeNotiById() {
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("matched")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("canceled")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("matched")
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .notiType("imminent")
                .isChecked(false)
                .slot(slot)
                .build());

        NotiDeleteDto deleteDto = NotiDeleteDto.builder()
                .notiId(notiRepository.findAll().get(0).getId())
                .build();
        notiService.removeNotiById(deleteDto);
        Assertions.assertThat(notiRepository.findAll().size()).isEqualTo(3);
    }
}