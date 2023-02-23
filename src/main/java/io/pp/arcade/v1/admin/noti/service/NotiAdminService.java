package io.pp.arcade.v1.admin.noti.service;

import io.pp.arcade.v1.admin.noti.dto.NotiListResponseDto;
import io.pp.arcade.v1.admin.noti.dto.NotiResponseDto;
import io.pp.arcade.v1.admin.noti.repository.NotiAdminRepository;
import io.pp.arcade.v1.domain.noti.Noti;
import io.pp.arcade.v1.domain.noti.NotiMailSender;
import io.pp.arcade.v1.domain.noti.slackbot.SlackbotService;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.type.NotiType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotiAdminService {

    private final NotiAdminRepository notiAdminRepository;
    private final UserRepository userRepository;
    private final SlotRepository slotRepository;
    private final SlackbotService slackbotService;

    @Transactional(readOnly = true)
    public NotiListResponseDto getAllNoti(Pageable pageable) {
        Page<Noti> allNotifications = notiAdminRepository.findAll(pageable);
        Page<NotiResponseDto> responseDtos = allNotifications.map(NotiResponseDto::new);
        return new NotiListResponseDto(responseDtos.getContent(), responseDtos.getTotalPages(),
                responseDtos.getNumber() + 1);
    }

    public void addNotiToUser(String intraId, Integer slotId, String message, Boolean sendMail) {
        User findUser = userRepository.findByIntraId(intraId).orElseThrow();
        Slot slot;
        slot = (slotId == null)? null : slotRepository.findById(slotId).orElseThrow();
        Noti noti = Noti.builder()
                .user(findUser)
                .slot(slot)
                .type(NotiType.ANNOUNCE)
                .message(message)
                .isChecked(false)
                .build();
        notiAdminRepository.save(noti);
        if(sendMail)
            slackbotService.sendSlackNoti(findUser.getIntraId(), noti);
    }

    public void addNotiToAll(String message, Boolean sendMail) {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            Noti noti = Noti.builder()
                    .user(user)
                    .type(NotiType.ANNOUNCE)
                    .message(message)
                    .isChecked(false)
                    .build();
            notiAdminRepository.save(noti);
            if (sendMail)
                slackbotService.sendSlackNoti(user.getIntraId(), noti);
        });
    }

    @Transactional(readOnly = true)
    public NotiListResponseDto getFilteredNotifications(Pageable pageable, String intraId) {
        Page<Noti> findNotis = notiAdminRepository.findNotisByUserIntraId(pageable, intraId);
        Page<NotiResponseDto> notiResponseDtoPage = findNotis.map(NotiResponseDto::new);
        return new NotiListResponseDto(
                notiResponseDtoPage.getContent(), notiResponseDtoPage.getTotalPages(),
                notiResponseDtoPage.getNumber() + 1
        );
    }
}
