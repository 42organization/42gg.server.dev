package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.RankAllDto;
import io.pp.arcade.domain.admin.dto.create.RankCreateRequestDto;
import io.pp.arcade.domain.admin.dto.delete.RankDeleteDto;
import io.pp.arcade.domain.admin.dto.update.RankUpdateRequestDto;
import io.pp.arcade.domain.rank.RankService;
import io.pp.arcade.domain.rank.dto.RankDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class RankAdminControllerImpl implements RankAdminController {
    private final RankService rankService;

    @Override
    @PostMapping(value = "/rank")
    public void rankCreate(RankCreateRequestDto createRequestDto, HttpServletRequest request) {
        rankService.createRankByAdmin(createRequestDto);
    }

    @Override
    @PutMapping(value = "/rank")
    public void rankUpdate(RankUpdateRequestDto updateRequestDto, HttpServletRequest request) {
        rankService.updateRankByAdmin(updateRequestDto);
    }

    @Override
    @DeleteMapping(value = "/rank/{id}")
    public void rankDelete(Integer rankId, HttpServletRequest request) {
        RankDeleteDto deleteDto = RankDeleteDto.builder().rankId(rankId).build();
        rankService.deleteRankByAdmin(deleteDto);
    }

    @Override
    @GetMapping(value = "/rank")
    public List<RankDto> rankAll(Pageable pageable, HttpServletRequest request) {
        List<RankDto> ranks = rankService.findRankByAdmin(pageable);
        return ranks;
    }
}
