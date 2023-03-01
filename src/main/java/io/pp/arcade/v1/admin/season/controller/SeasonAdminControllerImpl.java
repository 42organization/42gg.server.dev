package io.pp.arcade.v1.admin.season.controller;

import io.jsonwebtoken.lang.Collections;
import io.pp.arcade.v1.admin.rank.service.AdminRankRedisService;
import io.pp.arcade.v1.admin.season.dto.SeasonCreateRequestDto;
import io.pp.arcade.v1.admin.season.dto.SeasonAdminDto;
import io.pp.arcade.v1.admin.season.dto.SeasonListAdminResponseDto;
import io.pp.arcade.v1.admin.season.dto.SeasonUpdateRequestDto;
import io.pp.arcade.v1.admin.season.service.SeasonAdminService;
import io.pp.arcade.v1.domain.rank.dto.RankDto;
import io.pp.arcade.v1.domain.rank.service.RankRedisService;
import io.pp.arcade.v1.domain.rank.service.RankService;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.Mode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/pingpong/admin")
@AllArgsConstructor
public class SeasonAdminControllerImpl implements SeasonAdminController {
    private final SeasonAdminService seasonAdminService;
    private final AdminRankRedisService adminRankRedisService;

    @GetMapping(value = "/season/{seasonMode}")
    public SeasonListAdminResponseDto rankSeasonList(Mode seasonMode) {
        List<SeasonAdminDto> seasons = seasonAdminService.findAllSeasonByMode(seasonMode);

        SeasonListAdminResponseDto responseDto = SeasonListAdminResponseDto.builder()
                .seasonMode(Mode.BOTH.toString())
                .seasonList(seasons)
                .build();
        return responseDto;
    }

    @PostMapping(value = "/season")
    public void createSeason(SeasonCreateRequestDto seasonCreateReqeustDto) {
        if (seasonCreateReqeustDto.getStartTime().isBefore(LocalDateTime.now()))
            throw new BusinessException("E0001");
        Integer seasonId = seasonAdminService.createSeason(seasonCreateReqeustDto);
        SeasonDto seasonDto = seasonAdminService.findSeasonById(seasonId);
        if ((seasonDto.getSeasonMode() == Mode.BOTH || seasonDto.getSeasonMode() == Mode.RANK)
                && LocalDateTime.now().isBefore(seasonDto.getStartTime()))
            adminRankRedisService.addAllUserRankByNewSeason(seasonDto, seasonDto.getStartPpp());
    }

    @DeleteMapping(value = "/season/{seasonId}")
    public void deleteSeason(Integer seasonId) {
        SeasonDto seasonDto = seasonAdminService.findSeasonById(seasonId);
        seasonAdminService.deleteSeason(seasonId);
        if ((seasonDto.getSeasonMode() == Mode.BOTH || seasonDto.getSeasonMode() == Mode.RANK)
                && LocalDateTime.now().isBefore(seasonDto.getStartTime()))
            adminRankRedisService.deleteSeasonRankBySeasonId(seasonDto.getId());
    }

    @PutMapping(value = "/season/{seasonId}")
    public void updateSeason(Integer seasonId, SeasonUpdateRequestDto seasonUpdateRequestDto) {
        seasonAdminService.updateSeason(seasonId, seasonUpdateRequestDto);
        SeasonDto seasonDto = seasonAdminService.findSeasonById(seasonId);
        if ((seasonDto.getSeasonMode() == Mode.BOTH || seasonDto.getSeasonMode() == Mode.RANK)
            && LocalDateTime.now().isBefore(seasonDto.getStartTime())) {
            adminRankRedisService.deleteSeasonRankBySeasonId(seasonDto.getId());
            adminRankRedisService.addAllUserRankByNewSeason(seasonDto, seasonDto.getStartPpp());
        }
    }
}
