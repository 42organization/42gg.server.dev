package io.pp.arcade.global.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusType implements Constant{
    LIVE("LIVE"),
    WAIT("WAIT"),
    END("END");

    private final String code;

    @JsonCreator
    public static StatusType getEnumFromValue(String value) {
        for(StatusType e : values()) {
            if(e.name().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
