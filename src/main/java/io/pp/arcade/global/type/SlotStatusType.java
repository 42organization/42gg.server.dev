package io.pp.arcade.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SlotStatusType implements Constant {
    OPEN("open"),
    CLOSE("close"),
    MYTABLE("myTable");

    private final String code;
}
