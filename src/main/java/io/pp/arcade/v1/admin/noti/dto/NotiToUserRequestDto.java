package io.pp.arcade.v1.admin.noti.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotiToUserRequestDto {
    String intraId;
    Integer slotId;
    String message;
    Boolean sendMail;
}
