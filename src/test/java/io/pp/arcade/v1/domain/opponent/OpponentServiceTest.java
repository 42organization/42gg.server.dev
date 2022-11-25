package io.pp.arcade.v1.domain.opponent;

import io.pp.arcade.v1.domain.opponent.dto.OpponentResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class OpponentServiceTest {

    @Autowired
    private OpponentRepository opponentRepository;

    @Autowired
    private OpponentService opponentService;

    @Test
    void 유저가_조회된다() {
        //given
        Opponent opponent1 = opponentRepository.save(new Opponent("salee2", "sal", "http:google.com", "hihi", true));
        //when
        OpponentResponseDto responseDto = opponentService.findByIntraId(opponent1.getIntraId());

        //then
        assertThat(responseDto).isEqualTo(OpponentResponseDto.from(opponent1));
        assertThat(responseDto.getIntraId()).isEqualTo(opponent1.getIntraId());
        assertThat(responseDto.getNick()).isEqualTo(opponent1.getNick());
        assertThat(responseDto.getDetail()).isEqualTo(opponent1.getDetail());
    }

    @Test
    void 랜덤3유저() {
        for (int i = 0; i < 12; i++) {
            opponentRepository.save(new Opponent("id" + i, "nick" + i, "", "hihi", i % 3 != 0));
        }
        opponentService.findRandom3Opponents().stream().map(OpponentResponseDto::toString).forEach(System.out::println);
    }

}