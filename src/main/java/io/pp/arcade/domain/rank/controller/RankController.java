package io.pp.arcade.domain.rank.controller;

import io.pp.arcade.domain.rank.dto.RankListResponseDto;
import io.pp.arcade.global.util.RacketType;
import org.springframework.data.domain.Pageable;

public interface RankController {
        RankListResponseDto rankList(Pageable pageable, String type, Integer userId);
}
