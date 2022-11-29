package io.pp.arcade.v1.domain.rank.controller;

import io.pp.arcade.v1.domain.rank.dto.RankListRequestDto;
import io.pp.arcade.v1.domain.rank.dto.RankListResponseDto;
import io.pp.arcade.v1.domain.rank.dto.VipListResponseDto;
import io.pp.arcade.v1.global.type.GameType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public interface RankController {
        RankListResponseDto rankList(@SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable GameType gametype, @ModelAttribute RankListRequestDto requestDto, HttpServletRequest request);
        VipListResponseDto vipList(@SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false, defaultValue = "20") Integer count, HttpServletRequest request);
}
