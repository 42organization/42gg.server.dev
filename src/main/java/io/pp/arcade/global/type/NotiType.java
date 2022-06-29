package io.pp.arcade.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotiType implements Constant{
    MATCHED("matched"),
    CANCELEDBYMAN("canceledByMan"),
    CANCELEDBYTIME("canceledByTime"),
    IMMINENT("imminent"),
    ANNOUNCE("announce");

    private final String code;
}
