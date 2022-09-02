package io.pp.arcade.domain.noti;

import io.pp.arcade.TestInitiator;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.noti.Noti;
import io.pp.arcade.v1.domain.noti.NotiRepository;
import io.pp.arcade.v1.domain.noti.NotiService;
import io.pp.arcade.v1.domain.noti.dto.*;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.team.Team;
import io.pp.arcade.v1.domain.team.TeamRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.NotiType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

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

    @Autowired
    TestInitiator initiator;

    Slot slot;
    Team team1;
    User user1;
    User user2;
    Team team2;
    User user3;
    User user4;

    @BeforeEach
    void init() {
        initiator.letsgo();
        slot = initiator.slots[0];
        user1 = initiator.users[0];
        user2 = initiator.users[1];
        user3 = initiator.users[2];
        user4 = initiator.users[3];
        team1 = slot.getTeam1();
        team2 = slot.getTeam2();
    }


    @Test
    @Transactional
    void addNoti() throws MessagingException {
        NotiAddDto addDto = NotiAddDto.builder()
                .type(NotiType.ANNOUNCE)
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
                .type(NotiType.MATCHED)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.CANCELEDBYMAN)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.MATCHED)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.IMMINENT)
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
                .type(NotiType.MATCHED)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.CANCELEDBYMAN)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.MATCHED)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.IMMINENT)
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
                .type(NotiType.MATCHED)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.CANCELEDBYMAN)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.MATCHED)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.IMMINENT)
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
                .type(NotiType.MATCHED)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.CANCELEDBYMAN)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.MATCHED)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.IMMINENT)
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
                .type(NotiType.MATCHED)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.CANCELEDBYMAN)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.MATCHED)
                .isChecked(false)
                .slot(slot)
                .build());
        notiRepository.save(Noti.builder()
                .user(user1)
                .type(NotiType.IMMINENT)
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