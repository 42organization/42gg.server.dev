package io.pp.arcade.v1.admin.noti.dto;

import io.pp.arcade.v1.domain.noti.Noti;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.sql.Date;

@Getter
@NoArgsConstructor
public class NotiResponseDto {
    private Integer notiId;
    private String intraId;
    private Integer slotId;
    private String type;
    private Date createdTime;
    private String message;
    private boolean isChecked;

    public NotiResponseDto(Noti noti) {
        this.notiId = noti.getId();
        this.intraId = noti.getUser().getIntraId();
        this.slotId = (noti.getSlot() == null) ? null : noti.getSlot().getId();
        this.type = noti.getType().getCode();
        this.createdTime = Date.valueOf(noti.getCreatedAt().toLocalDate());
        this.isChecked = noti.getIsChecked();
        this.message = noti.getMessage();
    }
}
