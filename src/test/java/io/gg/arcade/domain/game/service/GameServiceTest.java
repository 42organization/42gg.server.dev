package io.gg.arcade.domain.game.service;

import io.gg.arcade.domain.game.dto.GameAddRequestDto;
import io.gg.arcade.domain.game.dto.GameModifyRequestDto;
import io.gg.arcade.domain.game.entity.Game;
import io.gg.arcade.domain.game.repository.GameRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class GameServiceTest {
    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Test
    void addGame() {
        // given
        String team1UUID = String.valueOf(UUID.randomUUID());
        String team2UUID = String.valueOf(UUID.randomUUID());

        GameAddRequestDto addDto = GameAddRequestDto.builder()
                                .team1Id(team1UUID)
                                .team2Id(team2UUID)
                                .build();
        // when
        gameService.addGame(addDto);

        // then
        Game game = gameRepository.findAll().get(0);
        Assertions.assertThat(team1UUID).isEqualTo(game.getTeam1Id());
        Assertions.assertThat(team2UUID).isEqualTo(game.getTeam2Id());
    }

    @Test
    @Transactional
    void modifyGame() {
        // given
        final Integer team1Score = 1;
        final Integer team2Score = 2;

        String team1UUID = String.valueOf(UUID.randomUUID());
        String team2UUID = String.valueOf(UUID.randomUUID());

        GameAddRequestDto addDto = GameAddRequestDto.builder()
                .team1Id(team1UUID)
                .team2Id(team2UUID)
                .build();
        gameService.addGame(addDto);

        Game addGame = gameRepository.findAll().get(0);
        GameModifyRequestDto modifyDto = GameModifyRequestDto.builder()
                        .gameId(addGame.getId())
                        .team1Score(team1Score)
                        .team2Score(team2Score)
                        .build();
        // when
        gameService.modifyGame(modifyDto);

        // then
        Game modifyGame = gameRepository.findById(addGame.getId()).orElseThrow();
        Assertions.assertThat(team1Score).isEqualTo(modifyGame.getTeam1Score());
        Assertions.assertThat(false).isEqualTo(modifyGame.getTeam1Win());
        Assertions.assertThat(team2Score).isEqualTo(modifyGame.getTeam2Score());
        Assertions.assertThat(true).isEqualTo(modifyGame.getTeam2Win());
    }
}