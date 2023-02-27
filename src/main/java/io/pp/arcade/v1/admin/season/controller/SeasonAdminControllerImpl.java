package io.pp.arcade.v1.admin.season.controller;

import io.jsonwebtoken.lang.Collections;
import io.pp.arcade.v1.admin.season.dto.SeasonCreateRequestDto;
import io.pp.arcade.v1.admin.season.dto.SeasonAdminDto;
import io.pp.arcade.v1.admin.season.dto.SeasonListAdminResponseDto;
import io.pp.arcade.v1.admin.season.dto.SeasonUpdateRequestDto;
import io.pp.arcade.v1.admin.season.service.SeasonAdminService;
import io.pp.arcade.v1.domain.rank.dto.RankDto;
import io.pp.arcade.v1.domain.rank.service.RankRedisService;
import io.pp.arcade.v1.domain.rank.service.RankService;
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

    private final RankService rankService;
    private final RankRedisService rankRedisService;

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
        seasonAdminService.createSeason(seasonCreateReqeustDto);
        List<RankDto> rankList = rankService.findAll();
    }

    @DeleteMapping(value = "/season/{seasonId}")
    public void deleteSeason(Integer seasonId) {
        seasonAdminService.deleteSeason(seasonId);
    }

    @PutMapping(value = "/season/{seasonId}")
    public void updateSeason(Integer seasonId, SeasonUpdateRequestDto seasonUpdateRequestDto) {
        seasonAdminService.updateSeason(seasonId, seasonUpdateRequestDto);
    }
}
