package io.pp.arcade.domain.game;

import io.pp.arcade.domain.game.dto.GameAddRequestDto;
import io.pp.arcade.domain.game.dto.GameModifyStatusDto;
import io.pp.arcade.domain.slot.dto.SlotDto;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void addGame(GameAddRequestDto addDto) {
        SlotDto slotDto = addDto.getSlotDto();
        Team team1 = teamRepository.findById(slotDto.getTeam1().getId()).orElseThrow(() -> new IllegalArgumentException("?"));
        Team team2 = teamRepository.findById(slotDto.getTeam2().getId()).orElseThrow(() -> new IllegalArgumentException("?"));
        gameRepository.save(Game.builder()
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
}