package io.pp.arcade.v1.domain.season;

import io.pp.arcade.v1.domain.admin.dto.create.SeasonCreateRequestDto;
import io.pp.arcade.v1.domain.admin.dto.update.SeasonUpdateDto;
import io.pp.arcade.v1.domain.season.dto.SeasonCreateDto;
import io.pp.arcade.v1.domain.season.dto.SeasonDeleteDto;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.season.dto.SeasonNameDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.Mode;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SeasonService {
    private final SeasonRepository seasonRepository;

    @Transactional
    public void createSeasonByAdmin(SeasonCreateDto createDto) {
        Season lastSeason = seasonRepository.findFirstByOrderByIdDesc().orElse(null);

        if (lastSeason != null) {
            lastSeason.setEndTime(LocalDateTime.now().minusSeconds(1));
        }
        Season season = Season.builder()
                .seasonName(createDto.getSeasonName())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59))
                .startPpp(createDto.getStartPpp())
                .pppGap(createDto.getPppGap())
                .seasonMode(createDto.getSeasonMode()).build();
        seasonRepository.save(season);
    }

    @Transactional
    public void deleteSeasonByAdmin(SeasonDeleteDto deleteDto) {
        Season season = seasonRepository.findById(deleteDto.getSeasonId()).orElseThrow(() -> new BusinessException("E0001"));
        seasonRepository.delete(season);
    }

    @Transactional
    public SeasonDto findCurrentSeason() {
        LocalDateTime now = LocalDateTime.now();
//      Season season = seasonRepository.findSeasonByStartTimeIsBeforeAndEndTimeIsAfter(now, now).orElseThrow(() -> new BusinessException("E0001"));
        Season season = seasonRepository.findSeasonByStartTimeIsBeforeAndEndTimeIsAfter(now, now).orElse(null);
        if (season == null)
            return null;
        return SeasonDto.from(season);
    }

    @Transactional
    public SeasonDto findLatestRankSeason() {
        Season season = seasonRepository.findFirstBySeasonModeOrSeasonModeOrderByIdDesc(Mode.RANK, Mode.BOTH).orElseThrow(() -> new BusinessException("E0001"));
        return SeasonDto.from(season);
    }

    @Transactional
    public SeasonDto findSeasonById(Integer seasonId) {
        Season season = seasonRepository.findById(seasonId).orElse(null);
        return season != null ? SeasonDto.from(season) : findLatestRankSeason();
    }

    @Transactional
    public List<SeasonNameDto> findAllSeason() {
        List<Season> seasons =  seasonRepository.findAll();
        List<SeasonNameDto> dtoList = new ArrayList<>();

        for (Season season : seasons) {
            SeasonNameDto dto = SeasonNameDto.builder().id(season.getId()).name(season.getSeasonName()).build();
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Transactional
    public List<SeasonNameDto> findAllRankSeason() {
        List<Season> seasons =  seasonRepository.findAllBySeasonModeOrSeasonMode(Mode.RANK, Mode.BOTH);
        List<SeasonNameDto> dtoList = new ArrayList<>();

        for (Season season : seasons) {
            SeasonNameDto dto = SeasonNameDto.builder().id(season.getId()).name(season.getSeasonName()).build();
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Transactional
    public List<SeasonNameDto> findAllRankSeasonUntilCurrent() {
        LocalDateTime now = LocalDateTime.now();
        List<Season> seasons =  seasonRepository.findAllBySeasonModeOrSeasonModeAndStartTimeIsBeforeOrderByStartTimeDesc(Mode.RANK, Mode.BOTH, now);
        List<SeasonNameDto> dtoList = new ArrayList<>();

        for (Season season : seasons) {
            SeasonNameDto dto = SeasonNameDto.builder().id(season.getId()).name(season.getSeasonName()).build();
            dtoList.add(dto);
        }
        return dtoList;
    }


    @Transactional
    public List<SeasonDto> findSeasonsByAdmin(Pageable pageable) {
        Page<Season> seasons = seasonRepository.findAllByOrderByIdDesc(pageable);
        List<SeasonDto> seasonDtos = seasons.stream().map(SeasonDto::from).collect(Collectors.toList());
        return seasonDtos;
    }

    @Transactional
    public void updateSeasonByAdmin(SeasonUpdateDto seasonUpdateDto) {
        Season season = seasonRepository.findById(seasonUpdateDto.getId()).orElseThrow(() -> new BusinessException("E0001"));
        season.setPppGap(seasonUpdateDto.getPppGap());
        season.setStartPpp(seasonUpdateDto.getStartPpp());
    }
}
