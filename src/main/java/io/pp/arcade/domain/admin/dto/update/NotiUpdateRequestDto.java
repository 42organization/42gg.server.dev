package io.pp.arcade.domain.admin.dto.update;

import lombok.Getter;

@Getter
public class NotiUpdateRequestDto {
    private Integer notiId;
    private Integer userId;
    private Integer slotId;
    private String notiType;
    private Boolean isChecked;
    private String message;
}
