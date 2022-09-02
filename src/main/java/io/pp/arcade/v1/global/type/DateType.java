package io.pp.arcade.v1.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DateType implements Constant{
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly");

    private final String code;
}
