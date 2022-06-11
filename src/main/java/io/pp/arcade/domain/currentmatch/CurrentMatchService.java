package io.pp.arcade.domain.currentmatch;

import io.pp.arcade.domain.currentmatch.dto.CurrentMatchAddDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchModifyDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchSaveGameDto;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class CurrentMatchService {
    private final CurrentMatchRepository currentMatchRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final SlotRepository slotRepository;

    @Transactional
    public void AddCurrentMatch(CurrentMatchAddDto addDto){
        User user = userRepository.findById(addDto.getUserId()).orElseThrow();
        Slot slot = slotRepository.findById(addDto.getSlot().getId()).orElseThrow();
        currentMatchRepository.save(CurrentMatch.builder()
                        .user(user)
                        .slot(slot)
                        .matchImminent(false)
                        .isMatched(false)
                        .build());
    }

    @Transactional
    public void ModifyCurrentMatch(CurrentMatchModifyDto modifyDto){
        User user = userRepository.findById(modifyDto.getUserId()).orElseThrow();
        CurrentMatch currentMatch = currentMatchRepository.findByUser(user);
        currentMatch.setIsMatched(modifyDto.getIsMatched());
        currentMatch.setMatchImminent(modifyDto.getMatchImminent());
    }

    @Transactional
    public void SaveGameInCurrentMatch(CurrentMatchSaveGameDto saveDto) {
        Game game = gameRepository.findById(saveDto.getGameId()).orElseThrow();
        User user = userRepository.findById(saveDto.getUserId()).orElseThrow();
        CurrentMatch currentMatch = currentMatchRepository.findByUser(user);
        currentMatch.setGame(game);
    }

    @Transactional
    public CurrentMatchDto findCurrentMatchByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        CurrentMatch currentMatch = currentMatchRepository.findByUser(user);
        CurrentMatchDto dto = null;

        if (currentMatch != null) {
            dto = CurrentMatchDto.builder()
                    .gameId(currentMatch.getGame().getId())
                    .userId(user.getId())
                    .slot(SlotDto.from(currentMatch.getSlot()))
                    .isMatched(currentMatch.getIsMatched())
                    .build();
        }
        return dto;
    }

    @Transactional
    public void removeCurrentMatch(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        currentMatchRepository.deleteByUser(user);
    }
}
