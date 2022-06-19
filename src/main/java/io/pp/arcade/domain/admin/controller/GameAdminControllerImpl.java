package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.create.GameCreateDto;
import io.pp.arcade.domain.admin.dto.create.GameCreateRequestDto;
import io.pp.arcade.domain.admin.dto.delete.GameDeleteDto;
import io.pp.arcade.domain.admin.dto.update.GameUpdateDto;
import io.pp.arcade.domain.admin.dto.update.GameUpdateRequestDto;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.slot.dto.SlotDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class GameAdminControllerImpl implements GameAdminController {
    private final GameService gameService;
    private final SlotService slotService;

    @Override
    @PostMapping(value = "/game")
    public void gameCreate(GameCreateRequestDto createRequestDto, HttpServletRequest request) {
        SlotDto slotDto = slotService.findSlotById(createRequestDto.getSlotId());
        GameCreateDto createDto = GameCreateDto.builder()
                .slotId(slotDto.getId())
                .seasonId(createRequestDto.getSeasonId())
                .status(createRequestDto.getStatus())
                .build();
        gameService.createGameByAdmin(createDto);
    }

    @Override
    @PutMapping(value = "/game")
    public void gameUpdate(GameUpdateRequestDto updateRequestDto, HttpServletRequest request) {
        GameUpdateDto updateDto = GameUpdateDto.builder()
                .gameId(updateRequestDto.getGameId())
                .slotId(updateRequestDto.getSlotId())
                .team1Id(updateRequestDto.getTeam1Id())
                .team2Id(updateRequestDto.getTeam2Id())
                .type(updateRequestDto.getType())
                .time(updateRequestDto.getTime())
                .seasonId(updateRequestDto.getSeasonId())
                .status(updateRequestDto.getStatus())
                .build();
        gameService.updateGameByAdmin(updateDto);
    }

    @Override
    @DeleteMapping(value = "/game/{id}")
    public void gameDelete(Integer gameId, HttpServletRequest request) {
        GameDeleteDto deleteDto = GameDeleteDto.builder().gameId(gameId).build();
        gameService.deleteGameByAdmin(deleteDto);
    }

    @Override
    @GetMapping(value = "/game")
    public List<GameDto> gameAll(Pageable pageable, HttpServletRequest request) {
        List<GameDto> games = gameService.findGameByAdmin(pageable);
        return games;
    }
}
