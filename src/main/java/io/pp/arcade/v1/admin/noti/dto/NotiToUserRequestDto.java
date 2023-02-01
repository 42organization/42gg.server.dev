package io.pp.arcade.v1.admin.noti.dto;

import lombok.Getter;

@Getter
public class NotiToUserRequestDto {
    String type;
    String intraId;
    Integer slotId;
    String message;
    Boolean sendMail;
}
