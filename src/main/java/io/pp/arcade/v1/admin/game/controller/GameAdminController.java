package io.pp.arcade.v1.admin.game.controller;

import io.pp.arcade.v1.admin.game.dto.GameLogListAdminResponseDto;
import org.apache.http.HttpResponse;
import org.springframework.web.bind.annotation.RequestParam;

public interface GameAdminController {
    GameLogListAdminResponseDto gameFindBySeasonId(@RequestParam(value = "season", required = false) int seasonId,
                                                   @RequestParam(value = "page")int page,
                                                   @RequestParam(defaultValue = "20")int size,
                                                   HttpResponse httpResponse);

    GameLogListAdminResponseDto gameFindByIntraId(@RequestParam(value = "q", required = false) String keyword,
                                                  @RequestParam(value = "page")int page,
                                                  @RequestParam(defaultValue = "20")int size,
                                                  HttpResponse httpResponse);
}
