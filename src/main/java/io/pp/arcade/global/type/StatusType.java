package io.pp.arcade.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusType implements Constant{
    LIVE("LIVE"),
    WAIT("WAIT"),
    END("END");

    private final String code;
}
