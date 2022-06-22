package io.pp.arcade.global.type;

import io.pp.arcade.global.redis.Key;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameType implements Constant{
    SINGLE(Key.SINGLE), BUNGLE(Key.BUNGLE);

    private final String code;
}
