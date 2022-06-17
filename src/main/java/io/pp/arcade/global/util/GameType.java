package io.pp.arcade.global.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameType {
    SINGLE("SINGLE"), DOUBLE("DOUBLE");

    private final String key;
}
