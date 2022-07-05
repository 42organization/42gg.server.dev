package io.pp.arcade.domain.slot.dto;

import io.pp.arcade.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SlotRemoveUserDto {
    private Integer slotId;
    private String userId;
    private Integer exitUserPpp;
}
