package io.pp.arcade.v1.admin.season.service;

import io.pp.arcade.v1.admin.dto.update.SeasonUpdateDto;
import io.pp.arcade.v1.admin.season.dto.SeasonAdminDto;
import io.pp.arcade.v1.admin.season.dto.SeasonCreateRequestDto;
import io.pp.arcade.v1.admin.season.dto.SeasonUpdateRequestDto;
import io.pp.arcade.v1.admin.season.repository.SeasonAdminRepository;
import io.pp.arcade.v1.domain.rank.service.RankRedisService;
import io.pp.arcade.v1.domain.season.Season;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.Mode;
import lombok.AllArgsConstructor;
import org.apache.tomcat.jni.Local;
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
    private final Integer minSeasonLength = 1;

    @Transactional
    public List<SeasonAdminDto> findAllSeasonByMode(Mode seasonMode) {
        List<Season> seasons =  seasonAdminRepository.findAllBySeasonModeOrderByStartTimeDesc(seasonMode);
        List<SeasonAdminDto> dtoList = new ArrayList<>();
        for (Season season : seasons) {
            SeasonAdminDto dto = SeasonAdminDto.from(season);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Transactional
    public SeasonDto findSeasonById(Integer seasonId) {
        Season season = seasonAdminRepository.findById(seasonId).orElse(null);
        if (season == null)
            throw new BusinessException("E0001");
        return SeasonDto.from(season);
    }
    @Transactional
    public Integer createSeason(SeasonCreateRequestDto createDto) {
        Season newSeason = Season.builder()
                .seasonName(createDto.getSeasonName())
                .seasonMode(createDto.getSeasonMode())
                .startTime(createDto.getStartTime())
                .startPpp(createDto.getStartPpp())
                .pppGap(createDto.getPppGap())
                .build();
        insert(newSeason);
        seasonAdminRepository.save(newSeason);
        checkSeasonAtDB(createDto.getSeasonMode());
        return (newSeason.getId());
    }

    @Transactional
    public void deleteSeason(Integer seasonId) {
        Season season = seasonAdminRepository.findById(seasonId).orElseThrow(() -> new BusinessException("E0001"));
        detach(season);
        seasonAdminRepository.delete(season);
        checkSeasonAtDB(season.getSeasonMode());
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
            seasonAdminRepository.save(season);
            checkSeasonAtDB(updateDto.getSeasonMode());
        }
   }

   private void insert(Season season)
   {
       List<Season> beforeSeasons = seasonAdminRepository.findBeforeSeasons(season.getSeasonMode(), season.getStartTime());
       Season beforeSeason;
       if (beforeSeasons.isEmpty())
           beforeSeason = null;
       else
           beforeSeason = beforeSeasons.get(0).getId() != season.getId()? beforeSeasons.get(0) : beforeSeasons.get(1);
       List<Season> afterSeasons = seasonAdminRepository.findAfterSeasons(season.getSeasonMode(), season.getStartTime());
       Season afterSeason = afterSeasons.isEmpty() ? null : afterSeasons.get(0);

       if (LocalDateTime.now().plusMinutes(1).isAfter(season.getStartTime()))
           throw new BusinessException("E0001");
       if (beforeSeason != null) {
           if (beforeSeason.getStartTime().plusDays(1).isAfter(season.getStartTime()))
               throw new BusinessException("E0001");
           beforeSeason.setEndTime(season.getStartTime().minusSeconds(1));
       }
       if (afterSeason != null)
           season.setEndTime(afterSeason.getStartTime().minusSeconds(1));
       else
           season.setEndTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
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
   }

   private void checkSeasonAtDB(Mode seasonMode)
   {
       List<Season> seasons =  seasonAdminRepository.findAllBySeasonModeOrderByStartTimeDesc(seasonMode);
       for (int i = 0; i < seasons.size(); i++) {
           for (int j = i + 1; j < seasons.size(); j++) {
               if (isOverlap(seasons.get(i), seasons.get(j)))
                   throw new BusinessException("E0001");
           }
       }
   }

    private boolean isOverlap(Season season1, Season season2) {
        LocalDateTime start1 = season1.getStartTime();
        LocalDateTime end1 = season1.getEndTime();
        LocalDateTime start2 = season2.getStartTime();
        LocalDateTime end2 = season2.getEndTime();

        if (start1.isEqual(end1) || start2.isEqual(end2)) {
            return false;
        }
        // 첫 번째 기간이 두 번째 기간의 이전에 끝날 때
        if (end1.isBefore(start2)) {
            return false;
        }

        // 첫 번째 기간이 두 번째 기간의 이후에 시작할 때
        if (start1.isAfter(end2)) {
            return false;
        }

        // 나머지 경우에는 두 기간이 겹칩니다.
        return true;
    }
}
