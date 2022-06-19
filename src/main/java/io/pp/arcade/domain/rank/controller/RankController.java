package io.pp.arcade.domain.rank.controller;

import io.pp.arcade.domain.rank.dto.RankListResponseDto;
import io.pp.arcade.global.type.GameType;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

public interface RankController {
        RankListResponseDto rankList(Pageable pageable, @PathVariable GameType gametype, HttpServletRequest request);
}
