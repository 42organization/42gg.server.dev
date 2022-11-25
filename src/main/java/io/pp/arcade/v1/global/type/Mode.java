package io.pp.arcade.v1.global.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

@Getter
@RequiredArgsConstructor
public enum Mode implements Constant{
    BOTH(0,"both"), NORMAL(1,"normal"), RANK(2,"rank"), CHALLENGE(3, "challenge");
    // 모드는 3가지가 있음.
    // type 형태이기 때문에 global 안에 type 도메인 안에 넣었음

    private final Integer value;
    private final String code;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Mode getEnumValue(String value) {
        for(Mode e : values()) {
            if(e.code.equals(value)) {
                return e;
            }
            else if (e.code.toUpperCase(Locale.ROOT).equals(value.toUpperCase(Locale.ROOT)))
                return e;
        }
        return null;
    }

}
