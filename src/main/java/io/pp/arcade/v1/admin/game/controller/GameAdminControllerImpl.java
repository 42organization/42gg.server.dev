package io.pp.arcade.v1.admin.game.controller;

import io.pp.arcade.v1.admin.game.dto.GameLogListAdminResponseDto;
import io.pp.arcade.v1.admin.game.service.GameAdminService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("pingpong/admin/games")
public class GameAdminControllerImpl implements GameAdminController{

    private final GameAdminService gameAdminService;
    @Override
    @GetMapping
    public GameLogListAdminResponseDto gameAll(int seasonId, int page, int size, HttpResponse httpResponse) {
        if (page < 1 || size < 1){
            httpResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            return null;
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        return gameAdminService.findAllGameByAdmin(pageable);
    }
}
