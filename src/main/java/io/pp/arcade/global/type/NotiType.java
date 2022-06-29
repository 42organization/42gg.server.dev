package io.pp.arcade.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotiType implements Constant{
    MATCHED("MATCHED"),
    CANCELEDBYMAN("CANCELEDBYMAN"),
    CANCELEDBYTIME("CANCELEDBYTIME"),
    IMMINENT("IMMINENT"),
    ANNOUNCE("ANNOUNCE");

    private final String code;
}
