package io.pp.arcade.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RacketType implements Constant{
    PENHOLDER("PENHOLDER"),
    SHAKEHAND("SHAKEHAND"),
    DUAL("DUAL"),
    NONE("NONE");

    private final String code;
}
