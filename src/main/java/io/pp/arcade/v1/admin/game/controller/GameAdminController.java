package io.pp.arcade.v1.admin.game.controller;

import io.pp.arcade.v1.admin.game.dto.GameLogListAdminResponseDto;
import org.apache.http.HttpResponse;
import org.springframework.web.bind.annotation.RequestParam;

public interface GameAdminController {
    GameLogListAdminResponseDto gameAll(@RequestParam(value = "season") int seasonId,
                                        @RequestParam(value = "page") int page,
                                        @RequestParam(defaultValue = "20")int size,
                                        HttpResponse httpResponse);
}
