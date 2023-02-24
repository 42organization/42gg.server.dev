package io.pp.arcade.v1.admin.season.controller;

import io.pp.arcade.v1.admin.season.dto.SeasonCreateRequestDto;
import io.pp.arcade.v1.admin.season.dto.SeasonAdminDto;
import io.pp.arcade.v1.admin.season.dto.SeasonListAdminResponseDto;
import io.pp.arcade.v1.admin.season.service.SeasonAdminService;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.Mode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/pingpong/admin")
@AllArgsConstructor
public class SeasonAdminControllerImpl implements SeasonAdminController {
    private final SeasonAdminService seasonAdminService;

    @GetMapping(value = "/season")
    public SeasonListAdminResponseDto rankSeasonList() {
        List<SeasonAdminDto> seasons = seasonAdminService.findAllRankSeason();

        SeasonListAdminResponseDto responseDto = SeasonListAdminResponseDto.builder()
                .seasonMode(Mode.BOTH.toString())
                .seasonList(seasons)
                .build();
        return responseDto;
    }

    @PostMapping(value = "/season")
    public void addSeason(SeasonCreateRequestDto seasonCreateReqeustDto)
    {
        System.out.println(seasonCreateReqeustDto + "=========================================");
        if (seasonCreateReqeustDto.getStartTime().isBefore(LocalDateTime.now()))
            throw new BusinessException("E0001");
        seasonAdminService.createSeason(seasonCreateReqeustDto);
    }
}
