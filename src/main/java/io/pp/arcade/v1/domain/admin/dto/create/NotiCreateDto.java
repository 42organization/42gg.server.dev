package io.pp.arcade.v1.domain.admin.dto.create;

import io.pp.arcade.v1.global.type.NotiType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotiCreateDto {
    private Integer userId;
    private Integer slotId;
    private NotiType notiType;
    private Boolean isChecked;
    private String message;
}
