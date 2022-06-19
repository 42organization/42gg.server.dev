package io.pp.arcade.global.util;

import io.pp.arcade.domain.noti.NotiService;
import io.pp.arcade.domain.noti.dto.NotiAddDto;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.global.type.GameType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class NotiGenerater {
    private final NotiService notiService;

    public void addMatchNotisBySlot(SlotDto slot) throws MessagingException {
        Integer maxSlotHeadCount = GameType.SINGLE.toString().equals(slot.getType()) ? 2 : 4;
        Boolean isMatched = slot.getHeadCount().equals(maxSlotHeadCount) ? true : false;
        Boolean isImminent = isMatched && slot.getTime().isBefore(LocalDateTime.now().plusMinutes(5)) ? true : false;
        if (isImminent == true) {
            addNoti(slot.getTeam1().getUser1(), slot, "imminent");
            addNoti(slot.getTeam1().getUser2(), slot, "imminent");
            addNoti(slot.getTeam2().getUser1(), slot, "imminent");
            addNoti(slot.getTeam2().getUser2(), slot, "imminent");
        } else if (isMatched == true) {
            addNoti(slot.getTeam1().getUser1(), slot, "matched");
            addNoti(slot.getTeam1().getUser2(), slot, "matched");
            addNoti(slot.getTeam2().getUser1(), slot, "matched");
            addNoti(slot.getTeam2().getUser2(), slot, "matched");
        }
    }

    public void addCancelNotisBySlot(SlotDto slot) throws MessagingException {
        addNoti(slot.getTeam1().getUser1(), slot, "canceled");
        addNoti(slot.getTeam1().getUser2(), slot, "canceled");
        addNoti(slot.getTeam2().getUser1(), slot, "canceled");
        addNoti(slot.getTeam2().getUser2(), slot, "canceled");
    }

    private void addNoti(UserDto user, SlotDto slot, String type) throws MessagingException {
        if (user != null) {
            NotiAddDto notiAddDto = NotiAddDto.builder()
                    .user(user)
                    .slot(slot)
                    .notiType(type)
                    .build();
            notiService.addNoti(notiAddDto);
        }
    }
}
