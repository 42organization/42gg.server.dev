package io.pp.arcade.domain.season;

import io.pp.arcade.domain.season.dto.SeasonAddDto;
import io.pp.arcade.domain.season.dto.SeasonDeleteDto;
import io.pp.arcade.domain.season.dto.SeasonDto;
import io.pp.arcade.domain.season.dto.SeasonFindDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SealedObject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SeasonService {
    private final SeasonRepository seasonRepository;

    @Transactional
    public void addCurrentSeason(SeasonAddDto addDto) {
        seasonRepository.save(Season.builder()
                .seasonName(addDto.getSeasonName())
                .startTime(addDto.getStartTime())
                .endTime(addDto.getEndTime())
                .startPpp(addDto.getStartPpp())
                .build());
    }

    @Transactional
    public void deleteCurrentSeason(SeasonDeleteDto deleteDto) {
        Season season = seasonRepository.findById(deleteDto.getSeasonId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));
        seasonRepository.delete(season);
    }

    public SeasonDto findCurrentSeason(SeasonFindDto findDto) {
        Season season = seasonRepository.findByStartTimeAfterAndEndTimeBefore(findDto.getCurrentTime()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));
        return SeasonDto.from(season);
    }
}
