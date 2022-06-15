package io.pp.arcade.domain.noti;

import io.pp.arcade.domain.noti.dto.*;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
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
}
