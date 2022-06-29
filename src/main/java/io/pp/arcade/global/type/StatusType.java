package io.pp.arcade.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusType implements Constant{
    LIVE("live"),
    WAIT("wait"),
    END("end");

    private final String code;
}
