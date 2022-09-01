package io.pp.arcade.domain.season;

import io.pp.arcade.domain.season.dto.SeasonDto;
import io.pp.arcade.domain.season.dto.SeasonListResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController(value = "/pingpong")
@AllArgsConstructor
public class SeasonController {
    private final SeasonService seasonService;

    @GetMapping(value = "/seasonlist")
    public SeasonListResponseDto getSeasonList() {
        List<SeasonDto> seasons = seasonService.findAllSeason();

        List<String> seasonList = new ArrayList<>();
        for (SeasonDto season : seasons) {
            seasonList.add(season.getSeasonName());
        }
        SeasonListResponseDto responseDto = SeasonListResponseDto.builder().seasonList(seasonList).build();
        return responseDto;
    }
}
