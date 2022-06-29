package io.pp.arcade.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SlotStatusType implements Constant {
    OPEN("OPEN"),
    CLOSE("CLOSE"),
    MYTABLE("MYTABLE");

    private final String code;
}
