package io.pp.arcade.domain.pchange;

import io.pp.arcade.domain.pchange.dto.PChangeAddRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PChangeServiceTest {
    @Autowired
    PChangeRepository pChangeRepository;
    @Autowired
    PChangeService pChangeService;

    @Test
    @Transactional
    void addPChange() {
        //given
        PChangeAddRequestDto addDto = PChangeAddRequestDto.builder()
                .gameId(111)
                .userId(222)
                .pppChange(333)
                .pppResult(444)
                .build();

        //when
        pChangeService.addPChange(addDto);

        //then
        PChange pChange = pChangeRepository.findAll().get(0);
        Assertions.assertThat(pChange.getGameId()).isEqualTo(111);
        Assertions.assertThat(pChange.getUserId()).isEqualTo(222);
        Assertions.assertThat(pChange.getPppChange()).isEqualTo(333);
        Assertions.assertThat(pChange.getPppResult()).isEqualTo(444);
    }

    @Test
    @Transactional
    void findPChange() {
    }
}