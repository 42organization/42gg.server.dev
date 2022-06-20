package io.pp.arcade.global.type;

import io.pp.arcade.global.redis.Key;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameType {
    SINGLE(Key.SINGLE), BUNGLE(Key.BUNGLE);

    private final String key;
}
