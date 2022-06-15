package io.pp.arcade.domain.noti.dto;

import io.pp.arcade.domain.noti.Noti;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class NotiDto {
    private Integer id;
    private SlotDto slot;
    private String type;
    private Boolean isChecked;
    private String message;
    private LocalDateTime creatdDate;

    public static NotiDto from(Noti noti) {
        return NotiDto.builder()
                .id(noti.getId())
                .slot(SlotDto.from(noti.getSlot()))
                .type(noti.getNotiType())
                .isChecked(noti.getIsChecked())
                .message(noti.getMessage())
                .creatdDate(noti.getCreatedAt())
                .build();
    }
}
