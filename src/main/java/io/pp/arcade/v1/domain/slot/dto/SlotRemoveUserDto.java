package io.pp.arcade.v1.domain.slot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlotRemoveUserDto {
    private Integer slotId;
    private String intraId;
    private Integer exitUserPpp;

    @Override
    public String toString() {
        return "SlotRemoveUserDto{" +
                "slotId=" + slotId +
                ", intraId='" + intraId + '\'' +
                ", exitUserPpp=" + exitUserPpp +
                '}';
    }
}
