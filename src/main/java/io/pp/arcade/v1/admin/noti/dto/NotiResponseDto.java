package io.pp.arcade.v1.admin.noti.dto;

import io.pp.arcade.v1.domain.noti.Noti;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotiResponseDto {
    Integer notiId;
    String intraId;
    Integer slotId;
    String type;
    LocalDateTime createdTime;
    boolean isChecked;

    public NotiResponseDto(Noti noti) {
        this.notiId = noti.getId();
        this.intraId = noti.getUser().getIntraId();
        this.slotId = (noti.getSlot() == null) ? null : noti.getSlot().getId();
        this.type = noti.getType().getCode();
        this.createdTime = noti.getCreatedAt();
        this.isChecked = noti.getIsChecked();
    }
}
