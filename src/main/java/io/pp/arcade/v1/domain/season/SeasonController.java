package io.pp.arcade.v1.domain.season;

import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.season.dto.SeasonListResponseDto;
import io.pp.arcade.v1.domain.season.dto.SeasonNameDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/pingpong")
@AllArgsConstructor
public class SeasonController {
    private final SeasonService seasonService;

    @GetMapping(value = "/seasonlist")
    public SeasonListResponseDto getRankSeasonList(HttpServletRequest request) {
        List<SeasonNameDto> seasons = seasonService.findAllSeason();
        SeasonDto currentSeason = seasonService.findCurrentSeason();

        SeasonListResponseDto responseDto = SeasonListResponseDto.builder().seasonMode(currentSeason.getSeasonMode().getCode()).seasonList(seasons).build();
        return responseDto;
    }
}
