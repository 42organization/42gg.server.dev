package io.pp.arcade.domain.rank.service;

import io.pp.arcade.domain.user.User;

public interface RankNTService {
    public void userToRedisRank(User user);
}
