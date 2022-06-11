package io.pp.arcade.domain.currentmatch;

import io.pp.arcade.domain.currentmatch.dto.CurrentMatchAddDto;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrentMatchService {
    private final CurrentMatchRepository currentMatchRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final SlotRepository slotRepository;

    public void AddCurrentMatch(CurrentMatchAddDto addDto){
        Game game = gameRepository.findById(addDto.getGame().getId()).orElseThrow();
        Slot slot = slotRepository.findById(addDto.getSlot().getId()).orElseThrow();
        currentMatchRepository.save(CurrentMatch.builder()
                        .game(game)
                        .slot()
                        .build());
    }

    public CurrentMatchDto findCurrentMatchByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        CurrentMatch currentMatch = currentMatchRepository.findByUser(user);
        CurrentMatchDto dto = CurrentMatchDto.builder()
                .gameId(currentMatch.game.getId())
                .userId(user.getId())
                .slot(SlotDto.from(currentMatch.slot))
                .isMatched(currentMatch.getIsMatched())
                .build();
        return dto;
    }
}
