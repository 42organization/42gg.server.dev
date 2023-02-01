package io.pp.arcade.v1.admin.noti.service;

import io.pp.arcade.v1.admin.noti.dto.NotiAllResponseDto;
import io.pp.arcade.v1.admin.noti.dto.NotiResponseDto;
import io.pp.arcade.v1.admin.noti.dto.NotiToAllRequestDto;
import io.pp.arcade.v1.admin.noti.dto.NotiToUserRequestDto;
import io.pp.arcade.v1.admin.noti.repository.NotiAdminRepository;
import io.pp.arcade.v1.domain.noti.Noti;
import io.pp.arcade.v1.domain.noti.NotiMailSender;
import io.pp.arcade.v1.domain.noti.NotiService;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotiAdminService {

    private final NotiAdminRepository notiAdminRepository;
    private final UserRepository userRepository;
    private final SlotRepository slotRepository;
    private final NotiMailSender notiMailSender;

    @Transactional(readOnly = true)
    public NotiAllResponseDto getAllNoti(Pageable pageable) {
        Page<Noti> allNotifications = notiAdminRepository.findAll(pageable);
        Page<NotiResponseDto> responseDtos = allNotifications.map(NotiResponseDto::new);
        return new NotiAllResponseDto(responseDtos.getContent(), responseDtos.getTotalPages(),
                responseDtos.getNumber() + 1);
    }

    public void addNotiToUser(String intraId, Integer slotId, String type, String message,
                              Boolean sendMail) throws MessagingException {
        User findUser = userRepository.findByIntraId(intraId).orElseThrow();
        Slot slot;
        slot = (slotId == null)? null : slotRepository.findById(slotId).orElseThrow();
        Noti noti = Noti.builder()
                .user(findUser)
                .slot(slot)
                .type(NotiType.of(type))
                .message(message)
                .isChecked(false)
                .build();
        notiAdminRepository.save(noti);
        if(sendMail)
            notiMailSender.sendMail(noti, findUser);
    }

    public void addNotiToAll(String message, String code, Boolean sendMail) {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            Noti noti = Noti.builder()
                    .user(user)
                    .type(NotiType.of(code))
                    .message(message)
                    .isChecked(false)
                    .build();
            notiAdminRepository.save(noti);
            if (sendMail) {
                try {
                    notiMailSender.sendMail(noti, user);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
