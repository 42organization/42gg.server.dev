package io.pp.arcade.v1.domain.game;

import io.pp.arcade.v1.domain.admin.dto.create.GameCreateDto;
import io.pp.arcade.v1.domain.admin.dto.delete.GameDeleteDto;

import io.pp.arcade.v1.domain.game.dto.*;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.SeasonRepository;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.v1.domain.team.TeamRepository;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import io.pp.arcade.v1.global.type.StatusType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final SlotRepository slotRepository;
    private final SeasonRepository seasonRepository;
    private final SlotTeamUserRepository slotTeamUserRepository;

    @Transactional
    public GameDto findById(Integer gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new BusinessException("E0001"));
        GameDto dto = GameDto.from(game);
        return dto;
    }

    @Transactional
    public void addGame(GameAddDto addDto) {
        SlotDto slotDto = addDto.getSlotDto();
        Season season = seasonRepository.findSeasonByStartTimeIsBeforeAndEndTimeIsAfter(LocalDateTime.now(), LocalDateTime.now()).orElse(null);
        Slot slot = slotRepository.findById(slotDto.getId()).orElseThrow(() -> new BusinessException("E0001"));
        gameRepository.save(Game.builder()
                .slot(slot)
                .mode(slot.getMode() == Mode.CHALLENGE ? Mode.RANK : slot.getMode())
//                .type(slotDto.getType())
//                .time(slotDto.getTime())
                .status(StatusType.LIVE)
                .season(season == null ? 1 : season.getId()) //season 추가
                .build()
        );
    }

    @Transactional
    public void modifyGameStatus(GameModifyStatusDto modifyStatusDto) {
        Game game = gameRepository.findById(modifyStatusDto.getGame().getId()).orElseThrow(() -> new BusinessException("E0001"));
        game.setStatus(modifyStatusDto.getStatus());
    }

    @Transactional
    public GameDto findBySlot(Integer slotId) {
        Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new BusinessException("E0001"));
        GameDto game = GameDto.from(gameRepository.findBySlot(slot).orElseThrow(() -> new BusinessException("E0001")));
        return game;
    }

    @Transactional
    public GameDto findBySlotIdNullable(Integer slotId) {
        Game game = gameRepository.findBySlotId(slotId).orElse(null);
        GameDto gameDto = game == null ? null : GameDto.from(game);
        return gameDto;
    }

    @Transactional
    public GameResultListDto v1_findGamesAfterId(GameFindDto findDto) {
        Integer mode = (findDto.getMode() == null) ? null : findDto.getMode().getValue();
        Integer status = (findDto.getStatus() == null) ? null : findDto.getStatus().getValue();
        List<Game> games = gameRepository.findGameListOrderByIdDesc(findDto.getSeasonId(), findDto.getId(), mode, status, findDto.getCount() + 1);
        Integer count = games.size();
        if (count == findDto.getCount() + 1)
            games.remove(count - 1);
        List<GameDto> gameDtoList = games.stream().map(GameDto::from).collect(Collectors.toList());
        GameResultListDto resultPageDto = GameResultListDto.builder()
                .gameList(gameDtoList)
                .isLast(count < findDto.getCount() + 1)
                .build();
        return resultPageDto;
    }

    @Transactional
    public void createGameByAdmin(GameCreateDto createDto) {
        Slot slot = slotRepository.findById(createDto.getSlotId()).orElseThrow(null);
        Game game = Game.builder()
                .slot(slot)
//                .time(slot.getTime())
                .season(createDto.getSeasonId())
//                .time(slot.getTime())
//                .type(slot.getType())
                .status(createDto.getStatus()).build();
        gameRepository.save(game);
    }

    @Transactional
    public void deleteGameByAdmin(GameDeleteDto deleteDto){
        Game game = gameRepository.findById(deleteDto.getGameId()).orElseThrow(null);
        gameRepository.delete(game);
    }

    @Transactional
    public List<GameDto> findGameByAdmin(Pageable pageable) {
        Page<Game> games = gameRepository.findAll(pageable);
        List<GameDto> gameDtos = games.stream().map(GameDto::from).collect(Collectors.toList());
        return gameDtos;
    }

    @Transactional
    public List<GameDto> findGameByTypeByAdmin(Pageable pageable, GameType type) {
//        Page<Game> games = gameRepository.findAllByTypeOrderByIdDesc(pageable, type);
//        List<GameDto> gameDtos = games.stream().map(GameDto::from).collect(Collectors.toList());
//        return gameDtos;
        return null;
    }

    @Transactional
    public List<GameDto> findGameByStatusType(StatusType statusType) {
        List<Game> games = gameRepository.findAllByStatus(statusType);
        List<GameDto> gameDtos = games.stream().map(GameDto::from).collect(Collectors.toList());
        return gameDtos;
    }
}
