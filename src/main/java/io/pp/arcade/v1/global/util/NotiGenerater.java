package io.pp.arcade.v1.global.util;

import io.pp.arcade.v1.domain.noti.NotiService;
import io.pp.arcade.v1.domain.noti.dto.NotiAddDto;
import io.pp.arcade.v1.domain.noti.dto.NotiCanceledTypeDto;
import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.NotiType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class NotiGenerater {
    private final NotiService notiService;

    public void addMatchNotisBySlot(SlotDto slot) throws MessagingException {
        Integer maxSlotHeadCount = GameType.SINGLE.equals(slot.getType()) ? 2 : 4;
        Boolean isMatched = slot.getHeadCount().equals(maxSlotHeadCount) ? true : false;
        Boolean isImminent = isMatched && slot.getTime().isBefore(LocalDateTime.now().plusMinutes(5)) ? true : false;
        if (isImminent == true) {
            addNoti(null, slot, NotiType.IMMINENT);
        } else if (isMatched == true) {
            addNoti(null, slot, NotiType.MATCHED);
        }
    }

    public void addCancelNotisBySlot(NotiCanceledTypeDto canceledTypeDto) {
        SlotDto slot = canceledTypeDto.getSlotDto();
        NotiType notiType = canceledTypeDto.getNotiType();
        addNoti(null, slot, notiType);
    }

    private void addNoti(UserDto user, SlotDto slot, NotiType type) {
        if (user == null) {
            NotiAddDto notiAddDto = NotiAddDto.builder()
                    .slot(slot)
                    .type(type)
                    .build();
            notiService.addNoti(notiAddDto);
        }
    }
}
