package io.pp.arcade.domain.rank.controller;

import io.pp.arcade.domain.rank.dto.RankListRequestDto;
import io.pp.arcade.domain.rank.dto.RankListResponseDto;
import io.pp.arcade.global.util.GameType;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

public interface RankController {
        RankListResponseDto rankList(Pageable pageable, @PathVariable GameType gametype, Integer userId);
}
