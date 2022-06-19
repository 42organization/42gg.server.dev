package io.pp.arcade.domain.noti;

import io.pp.arcade.domain.admin.dto.create.NotiCreateDto;
import io.pp.arcade.domain.admin.dto.create.NotiCreateRequestDto;
import io.pp.arcade.domain.admin.dto.update.NotiUpdateRequestDto;
import io.pp.arcade.domain.noti.dto.*;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotiService {
    private final NotiRepository notiRepository;
    private final UserRepository userRepository;
    private final SlotRepository slotRepository;
    private final JavaMailSender javaMailSender;

    @Transactional
    public void addNoti(NotiAddDto notiAddDto) throws MessagingException {
        User user = userRepository.findById(notiAddDto.getUser().getId()).orElseThrow();
        Slot slot = null;
        if (notiAddDto.getSlot() != null) {
            slot = slotRepository.findById(notiAddDto.getSlot().getId()).orElseThrow();
        }
        Noti noti = Noti.builder()
                .user(user)
                .slot(slot)
                .notiType(notiAddDto.getNotiType())
                .message(notiAddDto.getMessage())
                .isChecked(false)
                .build();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("핑퐁요정으로부터 온 편지");
        helper.setTo(user.getEMail());
        helper.setText("New Notification : " + notiAddDto.getNotiType() + "\nYou Have New Noti in 42PingPong!");
        javaMailSender.send(message);
        notiRepository.save(noti);
    }

    @Transactional
    public List<NotiDto> findNotiByUser(NotiFindDto findDto) {
        User user = userRepository.findById(findDto.getUser().getId()).orElseThrow();
        List<Noti> notis = notiRepository.findAllByUser(user);
        List<NotiDto> notiDtoList = notis.stream().map(NotiDto::from).collect(Collectors.toList());
        return notiDtoList;
    }

    @Transactional
    public NotiCountDto countAllNByUser(NotiFindDto findDto) {
        User user = userRepository.findById(findDto.getUser().getId()).orElseThrow();
        Integer count = notiRepository.countAllNByUser(user);
        NotiCountDto countDto = NotiCountDto.builder().notiCount(count).build();
        return countDto;
    }

    @Transactional
    public void modifyNotiChecked(NotiModifyDto modifyDto) {
        User user = userRepository.findById(modifyDto.getUser().getId()).orElseThrow();
        List<Noti> notis = notiRepository.findAllByUser(user);
        notis.forEach(noti -> {noti.setIsChecked(true);});
    }

    @Transactional
    public void removeAllNotisByUser(NotiDeleteDto deleteDto) {
        User user = userRepository.findById(deleteDto.getUser().getId()).orElseThrow();
        notiRepository.deleteAllByUser(user);
    }

    @Transactional
    public void removeNotiById(NotiDeleteDto deleteDto) {
        notiRepository.deleteById(deleteDto.getNotiId());
    }

    @Transactional
    public void createNotiByAdmin(NotiCreateRequestDto createRequestDto) {
        Slot slot = slotRepository.findById(createRequestDto.getSlotId()).orElseThrow();
        User user = userRepository.findById(createRequestDto.getUserId()).orElseThrow();
        Noti noti = Noti.builder()
                .slot(slot)
                .user(user)
                .notiType(createRequestDto.getNotiType())
                .message(createRequestDto.getMessage())
                .isChecked(createRequestDto.getIsChecked())
                .build();
        notiRepository.save(noti);
    }

    @Transactional
    public void updateNotiByAdmin(NotiUpdateRequestDto updateRequestDto) {
        Noti noti = notiRepository.findById(updateRequestDto.getNotiId()).orElseThrow();
        noti.setIsChecked(updateRequestDto.getIsChecked()); // 더 고칠게 있을까요
    }

    @Transactional
    public void deleteNotibyAdmin(NotiDeleteDto deleteDto) {
        Noti noti = notiRepository.findById(deleteDto.getNotiId()).orElseThrow();
        notiRepository.delete(noti);
    }

    @Transactional
    public List<NotiDto> findNotiByAdmin(Pageable pageable) {
        Page<Noti> notis = notiRepository.findAll(pageable);
        List<NotiDto> notiDtos = notis.stream().map(NotiDto::from).collect(Collectors.toList());
        return notiDtos;
    }
}
