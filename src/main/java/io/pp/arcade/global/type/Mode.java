package io.pp.arcade.global.type;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

@Getter
@RequiredArgsConstructor
public enum Mode implements Constant{
    ALL("all"), NORMAL("normal"), RANK("rank");
    // 모드는 3가지가 있음.
    // type 형태이기 때문에 global 안에 type 도메인 안에 넣었음

    private final String code;

    @JsonCreator
    public static Mode getEnumValue(String value) {
        for(Mode e : values()) {
            if(e.name().equals(value)) {
                return e;
            }
            else if (e.code.toUpperCase(Locale.ROOT).equals(value.toUpperCase(Locale.ROOT)))
                return e;
        }
    return null;
    }

}
