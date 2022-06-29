package io.pp.arcade.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

@Getter
@RequiredArgsConstructor
public enum SlotStatusType implements Constant {
    OPEN("OPEN"),
    CLOSE("CLOSE"),
    MYTABLE("MYTABLE");

    private final String code;

    public String getCode() {
        return code.toLowerCase(Locale.ROOT);
    }
}
