package io.pp.arcade.v1.admin.season.service;

import io.pp.arcade.v1.admin.season.dto.SeasonAdminDto;
import io.pp.arcade.v1.admin.season.dto.SeasonCreateRequestDto;
import io.pp.arcade.v1.admin.season.repository.SeasonAdminRepository;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.Mode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SeasonAdminService {
    private final SeasonAdminRepository seasonAdminRepository;

    @Transactional
    public List<SeasonAdminDto> findAllRankSeason() {
        List<Season> seasons =  seasonAdminRepository.findAllBySeasonModeOrSeasonMode(Mode.RANK, Mode.BOTH);
        List<SeasonAdminDto> dtoList = new ArrayList<>();
        for (Season season : seasons) {
            SeasonAdminDto dto = SeasonAdminDto.from(season);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Transactional
    public void createSeason(SeasonCreateRequestDto createDto) {
        Season currentSeason = seasonAdminRepository.findFirstByModeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualOrderByCreateTimeDesc(
                createDto.getSeasonMode(), LocalDateTime.now());
        Season beforeSeason = seasonAdminRepository.findFirstByModeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualOrderByCreateTimeDesc(
                createDto.getSeasonMode(), createDto.getStartTime());
        Season afterSeason = seasonAdminRepository.findNearestAfterSeason(createDto.getSeasonMode(), createDto.getStartTime());
        Season newSeason = Season.builder()
                .seasonName(createDto.getSeasonName())
                .seasonMode(createDto.getSeasonMode())
                .startPpp(createDto.getStartPpp())
                .pppGap(createDto.getPppGap())
                .build();

        if (currentSeason != null && currentSeason.getStartTime().isAfter(createDto.getStartTime()))
            throw new BusinessException("E0001");
        if (beforeSeason != null) {
            beforeSeason.setEndTime(createDto.getStartTime().minusSeconds(1));
        }
        newSeason.setStartTime(createDto.getStartTime());
        if (afterSeason != null)
            newSeason.setEndTime(afterSeason.getStartTime().minusSeconds(1));
        else
            newSeason.setEndTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
        System.out.println(newSeason.getSeasonName() + " | " + newSeason.getSeasonMode() + " | " + newSeason.getStartTime() + " | " + newSeason.getEndTime()
                + " | " + newSeason.getStartPpp() + " | " + newSeason.getPppGap() + " 끄틋 ");
        seasonAdminRepository.save(newSeason);
    }
//    @Transactional
//    public List<SeasonAdminDto> findAllSeason() {
//        List<Season> seasons = seasonAdminRepository.findAllBySeasonModeOrSeasonMode(Mode.RANK, Mode.BOTH);
//        List<SeasonAdminDto> dtoList = new ArrayList<>();
//        for (Season season : seasons) {
//            SeasonAdminDto dto = SeasonAdminDto.from(season);
//            dtoList.add(dto);
//        }
//        return dtoList;
//    }
//
//
//
//    @Transactional
//    public void createSeasonByAdmin(SeasonCreateRequestDto createDto) {
//        Season lastSeason = seasonAdminRepository.findFirstByOrderByIdDesc().orElse(null);
//        if (lastSeason != null) {
//            lastSeason.setEndTime(LocalDateTime.now().minusSeconds(1));
//        }
//        Season season = Season.builder()
//                .seasonName(createDto.getSeasonName())
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59))
//                .startPpp(createDto.getStartPpp())
//                .pppGap(createDto.getPppGap())
//                .seasonMode(createDto.getSeasonMode()).build();
//        seasonAdminRepository.save(season);
//    }
//
//    @Transactional
//    public void deleteSeasonByAdmin(SeasonDeleteDto deleteDto) {
//        Season season = seasonAdminRepository.findById(deleteDto.getSeasonId()).orElseThrow(() -> new BusinessException("E0001"));
//        seasonAdminRepository.delete(season);
//    }
//
//    @Transactional
//    public SeasonDto findLatestRankSeason() {
//        Season season = seasonAdminRepository.findFirstBySeasonModeOrSeasonModeOrderByIdDesc(Mode.RANK, Mode.BOTH).orElseThrow(() -> new BusinessException("E0001"));
//        return SeasonDto.from(season);
//    }
//
//    @Transactional
//    public SeasonDto findSeasonById(Integer seasonId) {
//        Season season = seasonAdminRepository.findById(seasonId).orElse(null);
//        return season != null ? SeasonDto.from(season) : findLatestRankSeason();
//    }
//
//    @Transactional
//    public List<SeasonNameDto> findAllRankSeasonUntilCurrent() {
//        LocalDateTime now = LocalDateTime.now();
//        List<Season> seasons =  seasonAdminRepository.findAllBySeasonModeOrSeasonModeAndStartTimeIsBeforeOrderByStartTimeDesc(Mode.RANK, Mode.BOTH, now);
//        List<SeasonNameDto> dtoList = new ArrayList<>();
//
//        for (Season season : seasons) {
//            SeasonNameDto dto = SeasonNameDto.builder().id(season.getId()).name(season.getSeasonName()).build();
//            dtoList.add(dto);
//        }
//        return dtoList;
//    }
//
//
//    @Transactional
//    public List<SeasonDto> findSeasonsByAdmin(Pageable pageable) {
//        Page<Season> seasons = seasonAdminRepository.findAllByOrderByIdDesc(pageable);
//        List<SeasonDto> seasonDtos = seasons.stream().map(SeasonDto::from).collect(Collectors.toList());
//        return seasonDtos;
//    }
//
//    @Transactional
//    public void updateSeasonByAdmin(SeasonUpdateDto seasonUpdateDto) {
//        Season season = seasonAdminRepository.findById(seasonUpdateDto.getId()).orElseThrow(() -> new BusinessException("E0001"));
//        season.setPppGap(seasonUpdateDto.getPppGap());
//        season.setStartPpp(seasonUpdateDto.getStartPpp());
//    }
}
