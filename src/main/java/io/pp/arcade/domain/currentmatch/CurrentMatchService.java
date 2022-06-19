package io.pp.arcade.domain.currentmatch;

import io.pp.arcade.domain.admin.dto.create.CurrentMatchCreateRequestDto;
import io.pp.arcade.domain.admin.dto.delete.CurrentMatchDeleteDto;
import io.pp.arcade.domain.admin.dto.update.CurrentMatchUpdateRequestDto;
import io.pp.arcade.domain.currentmatch.dto.*;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CurrentMatchService {
    private final CurrentMatchRepository currentMatchRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final SlotRepository slotRepository;

    @Transactional
    public void addCurrentMatch(CurrentMatchAddDto addDto){
        User user = userRepository.findById(addDto.getUserId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
        Slot slot = slotRepository.findById(addDto.getSlot().getId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
        currentMatchRepository.save(CurrentMatch.builder()
                        .user(user)
                        .slot(slot)
                        .matchImminent(false)
                        .isMatched(false)
                        .build());
    }

    @Transactional
    public void modifyCurrentMatch(CurrentMatchModifyDto modifyDto){
        User user = userRepository.findById(modifyDto.getUserId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
        CurrentMatch currentMatch = currentMatchRepository.findByUser(user).orElse(null);
        currentMatch.setIsMatched(modifyDto.getIsMatched());
        currentMatch.setMatchImminent(modifyDto.getMatchImminent());
    }

    @Transactional
    public void saveGameInCurrentMatch(CurrentMatchSaveGameDto saveDto) {
        Game game = gameRepository.findById(saveDto.getGameId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
        User user = userRepository.findById(saveDto.getUserId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
        CurrentMatch currentMatch = currentMatchRepository.findByUser(user).orElse(null);
        currentMatch.setGame(game);
    }

    @Transactional
    public CurrentMatchDto findCurrentMatchByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("{invalid.request}"));
        CurrentMatch currentMatch = currentMatchRepository.findByUser(user).orElse(null);
        CurrentMatchDto dto = null;

        if (currentMatch != null) {
            dto = CurrentMatchDto.builder()
                    .game(currentMatch.getGame() == null ? null : GameDto.from(currentMatch.getGame()))
                    .userId(user.getId())
                    .slot(SlotDto.from(currentMatch.getSlot()))
                    .matchImminent(currentMatch.getMatchImminent())
                    .isMatched(currentMatch.getIsMatched())
                    .build();
        }
        return dto;
    }

    @Transactional
    public CurrentMatchDto findCurrentMatchByIntraId(String intraId) {
        User user = userRepository.findByIntraId(intraId).orElseThrow(() -> new BusinessException("{invalid.request}"));
        CurrentMatch currentMatch = currentMatchRepository.findByUser(user).orElse(null);
        CurrentMatchDto dto = null;

        if (currentMatch != null) {
            dto = CurrentMatchDto.builder()
                    .game(currentMatch.getGame() == null ? null : GameDto.from(currentMatch.getGame()))
                    .userId(user.getId())
                    .slot(SlotDto.from(currentMatch.getSlot()))
                    .matchImminent(currentMatch.getMatchImminent())
                    .isMatched(currentMatch.getIsMatched())
                    .build();
        }
        return dto;
    }

    @Transactional
    public List<CurrentMatchDto> findCurrentMatchByGame(CurrentMatchFindDto findDto) {
        Game game = gameRepository.findById(findDto.getGame().getId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
        List<CurrentMatch> matches = currentMatchRepository.findAllByGame(game);
        List<CurrentMatchDto> currentMatchDtos = matches.stream().map(CurrentMatchDto::from).collect(Collectors.toList());
        return currentMatchDtos;
    }

    @Transactional
    public void removeCurrentMatch(CurrentMatchRemoveDto removeDto) {
        User user = userRepository.findById(removeDto.getUserId()).orElseThrow(() -> new BusinessException("{invalid.request}"));
        currentMatchRepository.deleteByUser(user);
    }

    @Transactional
    public void createCurrentMatchByAdmin(CurrentMatchCreateRequestDto createRequestDto) {
        User user = userRepository.findById(createRequestDto.getUserId()).orElseThrow();
        Slot slot = slotRepository.findById(createRequestDto.getSlotId()).orElseThrow();
        Game game = gameRepository.findById(createRequestDto.getGameId()).orElseThrow();
        CurrentMatch currentMatch = CurrentMatch.builder()
                .user(user)
                .slot(slot)
                .game(game)
                .matchImminent(createRequestDto.getMatchImminent())
                .isMatched(createRequestDto.getIsMatched()).build();
        currentMatchRepository.save(currentMatch);
    }

    @Transactional
    public void updateCurrentMatchByAdmin(CurrentMatchUpdateRequestDto updateRequestDto) {
        CurrentMatch currentMatch = currentMatchRepository.findById(updateRequestDto.getCurrenMatchId()).orElseThrow();
        Game game = gameRepository.findById(updateRequestDto.getGameId()).orElseThrow();
        currentMatch.setGame(game);
        currentMatch.setMatchImminent(updateRequestDto.getMatchImminent());
        currentMatch.setIsMatched(updateRequestDto.getIsMatched());
    }

    @Transactional
    public void deleteCurrentMatchByAdmin(CurrentMatchDeleteDto deleteDto) {
        CurrentMatch currentMatch = currentMatchRepository.findById(deleteDto.getCurrentMatchId()).orElseThrow();
        currentMatchRepository.delete(currentMatch);
    }

    @Transactional
    public List<CurrentMatchDto> findCurrentMatchByAdmin(Pageable pageable) {
        Page<CurrentMatch> currentMatches = currentMatchRepository.findAll(pageable);
        List<CurrentMatchDto> currentMatchDtos = currentMatches.stream().map(CurrentMatchDto::from).collect(Collectors.toList());
        return currentMatchDtos;
    }
}
