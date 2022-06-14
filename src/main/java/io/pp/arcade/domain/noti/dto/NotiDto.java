package io.pp.arcade.domain.noti.dto;

import io.pp.arcade.domain.noti.Noti;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotiDto {
    private Integer id;
    private UserDto user;
    private SlotDto slot;
    private String type;
    private Boolean isChecked;
    private LocalDateTime creatdDate;

    public static NotiDto from(Noti noti) {
        return NotiDto.builder()
                .id(noti.getId())
                .user(UserDto.from(noti.getUser()))
                .slot(SlotDto.from(noti.getSlot()))
                .type(noti.getNotiType())
                .isChecked(noti.getIsChecked())
                .creatdDate(noti.getCreatedDate())
                .build();
    }
}
