package io.gg.arcade.domain.game.service;

import io.gg.arcade.domain.game.dto.GameAddRequestDto;
import io.gg.arcade.domain.game.dto.GameDto;
import io.gg.arcade.domain.game.dto.GameModifyRequestDto;
import io.gg.arcade.domain.game.entity.Game;
import io.gg.arcade.domain.game.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    @Transactional
    public GameDto findById(Integer gameId){
        Game game = gameRepository.findById(gameId).orElseThrow(RuntimeException::new);
        return GameDto.from(game);
    }

    @Transactional
    public void addGame(GameAddRequestDto dto){
        gameRepository.save(dto.toEntity());
    }

    @Transactional
    public GameDto modifyGame(GameModifyRequestDto dto){
        //팀 스코어, win 수정
        Integer team1Score = dto.getTeam1Score();
        Integer team2Score = dto.getTeam2Score();

        Game game = gameRepository.findById(dto.getGameId()).orElseThrow(RuntimeException::new);
        game.setTeam1Score(team1Score);
        game.setTeam2Score(team2Score);

        if (team1Score > team2Score) {
            game.setTeam1Win(true);
        } else {
            game.setTeam2Win(true);
        }
        return GameDto.from(game);
    }
}
