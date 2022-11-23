package io.pp.arcade.v1.domain.admin.controller;

import io.pp.arcade.v1.domain.admin.dto.GameAddRequestDto;
import io.pp.arcade.v1.domain.game.GameService;
import io.pp.arcade.v1.global.scheduler.GameGenerator;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
public class GuestGameGeneratorAdminControllerImpl implements GuestGameGeneratorAdminController{
    private GameGenerator gameGenerator;
    @Override
    public void gameAdd(GameAddRequestDto gameAddRequestDto, HttpServletRequest request) {
        Integer slotId = gameAddRequestDto.getSlotId();
        gameGenerator.gameGenerator(slotId);
    }
}
