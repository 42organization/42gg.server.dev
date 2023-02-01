package io.pp.arcade.v1.admin.noti.dto;

import lombok.Getter;
import ognl.BooleanExpression;

@Getter
public class NotiToAllRequestDto {
    private String type;
    private String message;
    private Boolean sendMail;
}
