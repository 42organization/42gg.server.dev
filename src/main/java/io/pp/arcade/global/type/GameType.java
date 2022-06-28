package io.pp.arcade.global.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.pp.arcade.global.redis.Key;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameType implements Constant{
    SINGLE(Key.SINGLE), BUNGLE(Key.BUNGLE);

    private final String code;

    @JsonCreator
    public static GameType getEnumFromValue(String value) {
        for(GameType e : values()) {
            if(e.name().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
