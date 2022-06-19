package io.pp.arcade.domain.season;

import io.pp.arcade.domain.admin.dto.create.SeasonCreateRequestDto;
import io.pp.arcade.domain.season.dto.SeasonAddDto;
import io.pp.arcade.domain.season.dto.SeasonDeleteDto;
import io.pp.arcade.domain.season.dto.SeasonDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
public class SeasonServiceTest {

    @Autowired
    SeasonService seasonService;
    @Autowired
    SeasonRepository seasonRepository;

    Season season_notYet;
    Season season_present;
    Season season_past;

    @BeforeEach
    void init() {
        LocalDateTime time_start = LocalDateTime.now().plusMonths(1);
        LocalDateTime time_end = LocalDateTime.now().plusMonths(3);

        season_notYet = seasonRepository.save(Season.builder()
                .seasonName("notYet")
                .startTime(time_start)
                .endTime(time_end)
                .startPpp(150)
                .build());

        season_present = seasonRepository.save(Season.builder()
                .seasonName("present")
                .startTime(time_start.minusMonths(2))
                .endTime(time_end.minusMonths(2))
                .startPpp(100)
                .build());

        season_past = seasonRepository.save(Season.builder()
                .seasonName("past")
                .startTime(time_start.minusMonths(4))
                .endTime(time_end.minusMonths(4))
                .startPpp(50)
                .build());
    }

    @Test
    @Transactional
    void addCurrentSeason() {
        SeasonCreateRequestDto dto = SeasonCreateRequestDto.builder()
                .seasonName("now")
                .startTime(season_present.getStartTime())
                .endTime(season_present.getEndTime())
                .startPpp(100)
                .build();

        seasonService.createSeasonByAdmin(dto);
        Season foundSeason = seasonRepository.findAll().get(3);

        Assertions.assertThat(foundSeason.getStartTime()).isEqualTo(season_present.getStartTime());
        Assertions.assertThat(foundSeason.getEndTime()).isEqualTo(season_present.getEndTime());
        Assertions.assertThat(foundSeason.getSeasonName()).isEqualTo("now");
        Assertions.assertThat(foundSeason.getStartPpp()).isEqualTo(100);
    }

    @Test
    @Transactional
    void deleteCurrentSeason() {
        SeasonDeleteDto deleteDto = SeasonDeleteDto.builder().seasonId(season_past.getId()).build();
        seasonService.deleteSeasonByAdmin(deleteDto);

        List<Season> foundSeasons = seasonRepository.findAll();
        Assertions.assertThat(foundSeasons.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void findCurrentSeason() {
        SeasonDto foundDto = seasonService.findCurrentSeason();

        Assertions.assertThat(foundDto.getSeasonName()).isEqualTo(season_present.getSeasonName());
        Assertions.assertThat(foundDto.getStartTime()).isEqualTo(season_present.getStartTime());
        Assertions.assertThat(foundDto.getEndTime()).isEqualTo(season_present.getEndTime());
        Assertions.assertThat(foundDto.getStartPpp()).isEqualTo(season_present.getStartPpp());
    }
}
