package io.pp.arcade.v1.domain.admin.dto.update;

import io.pp.arcade.v1.global.type.NotiType;
import lombok.Getter;

@Getter
public class NotiUpdateRequestDto {
    private Integer notiId;
    private Integer userId;
    private Integer slotId;
    private NotiType notiType;
    private Boolean isChecked;
    private String message;
}
