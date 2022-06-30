package io.pp.arcade.global.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

@Getter
@RequiredArgsConstructor
public enum NotiType implements Constant{
    MATCHED("matched"),
    CANCELEDBYMAN("canceledbyman"),
    CANCELEDBYTIME("canceledbytime"),
    IMMINENT("imminent"),
    ANNOUNCE("announce");

    private final String code;

    @JsonCreator
    public static NotiType getEnumFromValue(String value) {
        for(NotiType e : values()) {
            if(e.code.equals(value)) {
                return e;
            }
            else if (e.code.toUpperCase(Locale.ROOT).equals(value.toUpperCase(Locale.ROOT))) {
                return e;
            }
        }
        return null;
    }

}
