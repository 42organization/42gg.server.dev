package io.pp.arcade.domain.admin.dto.create;

import lombok.Getter;

@Getter
public class NotiCreateRequestDto {
    private Integer notiId;
    private Integer userId;
    private Integer slotId;
    private String notiType;
    private Boolean isChecked;
    private String message;
}
