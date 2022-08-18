package io.pp.arcade.global.type;


import com.fasterxml.jackson.annotation.JsonCreator;
import io.pp.arcade.global.redis.Key;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

@Getter
@RequiredArgsConstructor
public enum Mode implements Constant{
    // 모드는 3가지가 있음.
    // type 형태이기 때문에 global 안에 type 도메인 안에 넣었음
    ALL(Key.ALL), NORMAL(Key.NORMAL), RANK(Key.RANK);

    private final String code;

    @JsonCreator
    public static Mode getEnumValue(String value) {
//        if(Mode e : values()) {
//            if(e.code.equals(value)) {
//                retrun e;
//            }
//            else if (E.code.toUpperCase(Locale.ROOT).equals(value.toUpperCase(Locale.ROOT)))
//                return e;
//        }
    return null;
    }

}
