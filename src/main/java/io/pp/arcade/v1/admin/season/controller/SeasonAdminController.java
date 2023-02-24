package io.pp.arcade.v1.admin.season.controller;

import io.pp.arcade.v1.admin.season.dto.SeasonCreateRequestDto;
import io.pp.arcade.v1.admin.season.dto.SeasonListAdminResponseDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface SeasonAdminController {
    SeasonListAdminResponseDto rankSeasonList();
    void addSeason(@RequestBody SeasonCreateRequestDto seasonAddReqeustDto);
}
