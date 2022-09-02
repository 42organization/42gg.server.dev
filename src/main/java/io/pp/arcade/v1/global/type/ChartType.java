package io.pp.arcade.v1.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChartType implements Constant{
    RANK("RANK");

    private final String code;
}
