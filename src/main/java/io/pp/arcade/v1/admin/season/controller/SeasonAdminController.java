package io.pp.arcade.v1.admin.season.controller;

import io.pp.arcade.v1.admin.season.dto.SeasonCreateRequestDto;
import io.pp.arcade.v1.admin.season.dto.SeasonListAdminResponseDto;
import io.pp.arcade.v1.global.type.Mode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface SeasonAdminController {
    SeasonListAdminResponseDto rankSeasonList(@PathVariable Mode seasonMode);
    void createSeason(@RequestBody SeasonCreateRequestDto seasonAddReqeustDßto);

    void deleteSeason(@PathVariable Integer seasonId);
}
