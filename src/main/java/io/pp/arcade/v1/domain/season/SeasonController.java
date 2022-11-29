package io.pp.arcade.v1.domain.season;

import io.pp.arcade.v1.domain.admin.dto.create.SeasonCreateRequestDto;
import io.pp.arcade.v1.domain.season.dto.SeasonCreateDto;
import io.pp.arcade.v1.domain.season.dto.SeasonDto;
import io.pp.arcade.v1.domain.season.dto.SeasonListResponseDto;
import io.pp.arcade.v1.domain.season.dto.SeasonNameDto;
import io.pp.arcade.v1.global.type.Mode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
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
        List<SeasonNameDto> seasons = seasonService.findAllRankSeasonUntilCurrent();
        SeasonDto currentSeason = seasonService.findLatestRankSeason();

        SeasonListResponseDto responseDto = SeasonListResponseDto.builder().seasonMode(currentSeason.getSeasonMode().getCode()).seasonList(seasons).build();
        return responseDto;
    }

    @PostConstruct
    public void checkExistSeason() {
        SeasonDto seasonDto = seasonService.findCurrentSeason();
        SeasonCreateDto createDto = SeasonCreateDto.builder().seasonName("softwave").seasonMode(Mode.BOTH).pppGap(1000).startPpp(1000).build();
        if (seasonDto == null)
            seasonService.createSeasonByAdmin(createDto);
    }
}
