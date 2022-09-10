package io.pp.arcade.v1.domain.admin.dto.create;

import io.pp.arcade.v1.global.type.NotiType;
import lombok.Getter;

@Getter
public class NotiCreateRequestDto {
    private String userId;
    private Integer slotId;
    private NotiType notiType;
    private Boolean isChecked;
    private String message;
    private Boolean sendMail;
}
