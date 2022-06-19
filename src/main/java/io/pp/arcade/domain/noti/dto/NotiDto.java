package io.pp.arcade.domain.noti.dto;

import io.pp.arcade.domain.noti.Noti;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.global.type.NotiType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotiDto {
    private Integer id;
    private SlotDto slot;
    private NotiType type;
    private Boolean isChecked;
    private String message;
    private LocalDateTime creatdDate;

    public static NotiDto from(Noti noti) {
        return NotiDto.builder()
                .id(noti.getId())
                .slot(SlotDto.from(noti.getSlot()))
                .type(noti.getType())
                .isChecked(noti.getIsChecked())
                .message(noti.getMessage())
                .creatdDate(noti.getCreatedAt())
                .build();
    }
}
