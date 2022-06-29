package io.pp.arcade.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RacketType implements Constant{
    PENHOLDER("penholder"),
    SHAKEHAND("shakehand"),
    DUAL("dual"),
    NONE("none");

    private final String code;
}
