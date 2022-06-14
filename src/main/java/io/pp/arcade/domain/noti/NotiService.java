package io.pp.arcade.domain.noti;

import io.pp.arcade.domain.noti.dto.NotiAddDto;
import io.pp.arcade.domain.noti.dto.NotiDeleteDto;
import io.pp.arcade.domain.noti.dto.NotiDto;
import io.pp.arcade.domain.noti.dto.NotiFindDto;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotiService {
    private final NotiRepository notiRepository;
    private final UserRepository userRepository;
    private final SlotRepository slotRepository;

    @Transactional
    public void addNoti(NotiAddDto notiAddDto) {
        User user = userRepository.findById(notiAddDto.getUserDto().getId()).orElseThrow();
        Slot slot = slotRepository.findById(notiAddDto.getSlotDto().getId()).orElseThrow();
        Noti noti = Noti.builder()
                .user(user)
                .slot(slot)
                .notiType(notiAddDto.getNotiType())
                .message(notiAddDto.getMessage())
                .isChecked(false)
                .build();
        notiRepository.save(noti);
    }

    @Transactional
    public List<NotiDto> findNotiByUser(NotiFindDto findDto) {
        User user = userRepository.findById(findDto.getUser().getId()).orElseThrow();
        List<Noti> notis = notiRepository.findAllByUser(user);
        List<NotiDto> dtoList = notis.stream().map(NotiDto::from).collect(Collectors.toList());
        return dtoList;
    }

    @Transactional
    public void modifyNotiChecked(NotiFindDto findDto) {
        User user = userRepository.findById(findDto.getUser().getId()).orElseThrow();
        List<Noti> notis = notiRepository.findAllByUser(user);
        notis.forEach(noti -> {noti.setIsChecked(true);});
    }

    @Transactional
    public void deleteAllNotisByUser(NotiDeleteDto deleteDto) {
        User user = userRepository.findById(deleteDto.getUser().getId()).orElseThrow();
        notiRepository.deleteAllByUser(user);
    }
}
