package io.pp.arcade.domain.rank.controller;

import io.pp.arcade.domain.rank.dto.RankListResponseDto;
import io.pp.arcade.global.type.GameType;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;

public interface RankController {
        RankListResponseDto rankList(Pageable pageable, @RequestParam(required = false, defaultValue = "20") @Positive  Integer count, @PathVariable GameType gametype, HttpServletRequest request);
}
