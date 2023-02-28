package io.pp.arcade.v1.admin.season.service;

import io.pp.arcade.v1.admin.dto.update.SeasonUpdateDto;
import io.pp.arcade.v1.admin.season.dto.SeasonAdminDto;
import io.pp.arcade.v1.admin.season.dto.SeasonCreateRequestDto;
import io.pp.arcade.v1.admin.season.dto.SeasonUpdateRequestDto;
import io.pp.arcade.v1.admin.season.repository.SeasonAdminRepository;
import io.pp.arcade.v1.domain.rank.service.RankRedisService;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.Mode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
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
    public List<SeasonAdminDto> findAllSeasonByMode(Mode seasonMode) {
        List<Season> seasons =  seasonAdminRepository.findAllBySeasonMode(seasonMode);
        List<SeasonAdminDto> dtoList = new ArrayList<>();
        for (Season season : seasons) {
            SeasonAdminDto dto = SeasonAdminDto.from(season);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Transactional
    public void createSeason(SeasonCreateRequestDto createDto) {
        Season newSeason = Season.builder()
                .seasonName(createDto.getSeasonName())
                .seasonMode(createDto.getSeasonMode())
                .startTime(createDto.getStartTime())
                .startPpp(createDto.getStartPpp())
                .pppGap(createDto.getPppGap())
                .build();
        insert(newSeason);
        seasonAdminRepository.save(newSeason);
    }

    @Transactional
    public void deleteSeason(Integer seasonId) {
        Season season = seasonAdminRepository.findById(seasonId).orElseThrow(() -> new BusinessException("E0001"));
        detach(season);
        seasonAdminRepository.delete(season);
    }
    @Transactional
    public void updateSeason(Integer seasonId, SeasonUpdateRequestDto updateDto) {
        Season season = seasonAdminRepository.findById(seasonId).orElseThrow(() -> new BusinessException("E0001"));
        if (LocalDateTime.now().isBefore(season.getEndTime())) {
            season.setPppGap(updateDto.getPppGap());
        }
        if (LocalDateTime.now().isBefore(season.getStartTime())) {
            detach(season);
            season.setSeasonName(updateDto.getSeasonName());
            season.setSeasonMode(updateDto.getSeasonMode());
            season.setStartTime(updateDto.getStartTime());
            season.setStartPpp(updateDto.getStartPpp());
            insert(season);
        }
   }

   private void insert(Season season)
   {
       Season beforeSeason = seasonAdminRepository.findFirstByModeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
               season.getSeasonMode(), season.getStartTime());
       List<Season> afterSeasons = seasonAdminRepository.findAfterSeasons(season.getSeasonMode(), season.getStartTime());
       Season afterSeason = afterSeasons.isEmpty() ? null : afterSeasons.get(0);

       if (LocalDateTime.now().isAfter(season.getStartTime()))
           throw new BusinessException("E0001");
       if (beforeSeason != null) {
           if (beforeSeason.getStartTime().plusMinutes(1).isAfter(season.getStartTime()))
               throw new BusinessException("E0001");
           beforeSeason.setEndTime(season.getStartTime().minusSeconds(1));
       }
       if (afterSeason != null)
           season.setEndTime(afterSeason.getStartTime().minusSeconds(1));
       else
           season.setEndTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
       seasonAdminRepository.save(season);
   }

   private void detach(Season season)
   {
       List<Season> beforeSeasons = seasonAdminRepository.findBeforeSeasons(season.getSeasonMode(), season.getStartTime());
       Season beforeSeason = beforeSeasons.isEmpty() ? null : beforeSeasons.get(0);
       List<Season> afterSeasons = seasonAdminRepository.findAfterSeasons(season.getSeasonMode(), season.getStartTime());
       Season afterSeason = afterSeasons.isEmpty() ? null : afterSeasons.get(0);

       if ((LocalDateTime.now().isAfter(season.getStartTime()) && LocalDateTime.now().isBefore(season.getEndTime()))
               || season.getEndTime().isBefore(LocalDateTime.now()))
           throw new BusinessException("E0001");
       if (beforeSeason != null) {
           if (afterSeason != null)
               beforeSeason.setEndTime(afterSeason.getStartTime().minusSeconds(1));
           else
               beforeSeason.setEndTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
       }
       seasonAdminRepository.delete(season);
   }
}
