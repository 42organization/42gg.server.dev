package io.pp.arcade.v1.domain.rank.service;

import io.pp.arcade.v1.domain.user.dto.UserDto;

public interface RankNTService {
    void userToRedisRank(UserDto user);
}
