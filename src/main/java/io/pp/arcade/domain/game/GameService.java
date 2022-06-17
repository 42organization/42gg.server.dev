package io.pp.arcade.domain.game;

import io.pp.arcade.domain.admin.dto.create.GameCreateDto;
import io.pp.arcade.domain.game.dto.*;
import io.pp.arcade.domain.pchange.dto.PChangeDto;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import io.pp.arcade.domain.team.dto.TeamDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final SlotRepository slotRepository;

    @Transactional
    public GameDto findById(Integer gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("gameId를 찾을 수 없다."));
        GameDto dto = GameDto.from(game);
        return dto;
    }

    @Transactional
    public void addGame(GameAddDto addDto) {
        SlotDto slotDto = addDto.getSlotDto();
        Slot slot = slotRepository.findById(slotDto.getId()).orElseThrow();
        Team team1 = teamRepository.findById(slotDto.getTeam1().getId()).orElseThrow(() -> new IllegalArgumentException("?"));
        Team team2 = teamRepository.findById(slotDto.getTeam2().getId()).orElseThrow(() -> new IllegalArgumentException("?"));
        gameRepository.save(Game.builder()
                .slot(slot)
                .team1(team1)
                .team2(team2)
                .type(slotDto.getType())
                .time(slotDto.getTime())
                .status("live")
                .season(1) //season 추가
                .build()
        );
    }

    @Transactional
    public void modifyGameStatus(GameModifyStatusDto modifyStatusDto) {
        Game game = gameRepository.findById(modifyStatusDto.getGameId()).orElseThrow(() -> new IllegalArgumentException("?"));
        game.setStatus(modifyStatusDto.getStatus());
    }

    @Transactional
    public GameDto findBySlot(Integer slotId) {
        Slot slot = slotRepository.findById(slotId).orElseThrow();
        GameDto game = GameDto.from(gameRepository.findBySlot(slot).orElseThrow());
        return game;
    }

    @Transactional
    public GameResultPageDto findEndGames(Pageable page) {
        Page<Game> games = gameRepository.findByStatus("end", page);
        List<GameDto> gameDtoList = games.stream().map(GameDto::from).collect(Collectors.toList());

        GameResultPageDto resultPageDto = GameResultPageDto.builder().gameList(gameDtoList)
                                                .currentPage(games.getNumber() + 1)
                                                .totalPage(games.getTotalPages())
                                                .build();
        return resultPageDto;
    }

    public void gameCreateByAdmin(GameCreateDto createDto) {
        Slot slot = slotRepository.findById(createDto.getSlotId()).orElseThrow(null);
        Game game = Game.builder()
                .slot(slot)
                .team1(slot.getTeam1())
                .team2(slot.getTeam2())
                .time(slot.getTime())
                .season(createDto.getSeasonId())
                .time(slot.getTime())
                .type(slot.getType())
                .status(createDto.getStatus()).build();
        gameRepository.save(game);
    }
}