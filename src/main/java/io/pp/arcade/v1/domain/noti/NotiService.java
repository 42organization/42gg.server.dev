package io.pp.arcade.v1.domain.noti;

import io.pp.arcade.v1.admin.dto.create.NotiCreateRequestDto;
import io.pp.arcade.v1.domain.noti.dto.*;
import io.pp.arcade.v1.global.notification.NotiMailSender;
import io.pp.arcade.v1.global.notification.SnsNotiService;
import io.pp.arcade.v1.global.notification.slackbot.SlackbotService;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUser;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.NotiType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotiService {
    private final NotiRepository notiRepository;
    private final UserRepository userRepository;
    private final SlotRepository slotRepository;

    private final SnsNotiService snsNotiService;
    private final SlotTeamUserRepository slotTeamUserRepository;

    @Transactional
    public void addNoti(NotiAddDto notiAddDto) {
        Slot slot = null;
        if (notiAddDto.getSlot() != null) {
            slot = slotRepository.findById(notiAddDto.getSlot().getId()).orElseThrow(() -> new BusinessException("E0001"));
            List<SlotTeamUser> slotTeamUser = slotTeamUserRepository.findAllBySlotId(notiAddDto.getSlot().getId());
            for (SlotTeamUser users : slotTeamUser) {
                Noti noti = notiRepository.save(Noti.builder()
                        .slot(slot)
                        .user(users.getUser())
                        .type(notiAddDto.getType())
                        .message(notiAddDto.getMessage())
                        .isChecked(false)
                        .build());
                snsNotiService.sendSnsNotification(noti, users.getUser());
            }
        } else {
            User user = userRepository.findById(notiAddDto.getUser().getId()).orElseThrow(() -> new BusinessException("E0001"));
            Noti noti = notiRepository.save(Noti.builder()
                    .slot(slot)
                    .user(user)
                    .type(notiAddDto.getType())
                    .message(notiAddDto.getMessage())
                    .isChecked(false)
                    .build());
            snsNotiService.sendSnsNotification(noti, user);
        }
    }

    @Transactional
    public List<NotiDto> findNotiByUser(NotiFindDto findDto) {
        User user = userRepository.findById(findDto.getUser().getId()).orElseThrow(() -> new BusinessException("E0001"));
        List<Noti> notis = notiRepository.findAllByUserOrderByIdDesc(user);
        List<NotiDto> notiDtoList = notis.stream().map(NotiDto::from).collect(Collectors.toList());
        return notiDtoList;
    }

    @Transactional
    public NotiDto findNotiByIdAndUser(NotiFindDto findDto) {
        Noti noti = notiRepository.findByIdAndUserId(findDto.getNotiId(), findDto.getUser().getId()).orElseThrow(() -> new BusinessException("E0001"));
        return NotiDto.from(noti);
    }

    @Transactional
    public NotiCountDto countAllNByUser(NotiFindDto findDto) {
        User user = userRepository.findById(findDto.getUser().getId()).orElseThrow(() -> new BusinessException("E0001"));
        Integer count = notiRepository.countAllNByUserAndIsChecked(user, false);
        NotiCountDto countDto = NotiCountDto.builder().notiCount(count).build();
        return countDto;
    }

    @Transactional
    public void modifyNotiChecked(NotiModifyDto modifyDto) {
        User user = userRepository.findById(modifyDto.getUser().getId()).orElseThrow(() -> new BusinessException("E0001"));
        List<Noti> notis = notiRepository.findAllByUser(user);
        notis.forEach(noti -> {noti.setIsChecked(true);});
    }

    @Transactional
    public void removeAllNotisByUser(NotiDeleteDto deleteDto) {
        User user = userRepository.findById(deleteDto.getUser().getId()).orElseThrow(() -> new BusinessException("E0001"));
        notiRepository.deleteAllByUser(user);
    }

    @Transactional
    public void removeNotiById(NotiDeleteDto deleteDto) {
        notiRepository.deleteById(deleteDto.getNotiId());
    }

    @Transactional
    public void createNotiForAll(NotiCreateRequestDto createRequestDto) {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            Noti noti = Noti.builder()
                    .user(user)
                    .type(NotiType.ANNOUNCE)
                    .message(createRequestDto.getMessage())
                    .isChecked(false)
                    .build();
            notiRepository.save(noti);
        });
    }

    @Transactional
    public void createEventNotiForAll(NotiAddDto addDto) {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            Noti noti = Noti.builder()
                    .user(user)
                    .type(NotiType.ANNOUNCE)
                    .message(addDto.getMessage())
                    .isChecked(false)
                    .build();
            notiRepository.save(noti);
        });
    }

    @Transactional
    public void sendEventMail(NotiAddDto addDto) throws MessagingException {
        User user = userRepository.findById(addDto.getUser().getId()).orElse(null);
        Noti noti = Noti.builder()
                .user(user)
                .type(NotiType.ANNOUNCE)
                .message("축하합니다!! 이벤트에 당첨되었습니다🎉")
                .isChecked(false)
                .build();
        snsNotiService.sendSnsNotification(noti, user);
    }
}
