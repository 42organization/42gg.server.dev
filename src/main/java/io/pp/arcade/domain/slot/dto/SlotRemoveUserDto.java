package io.pp.arcade.domain.slot.dto;

import io.pp.arcade.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlotRemoveUserDto {
    private Integer slotId;
    private String userId;
    private Integer exitUserPpp;
}
