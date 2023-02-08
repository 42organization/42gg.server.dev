package io.pp.arcade.v1.admin.penalty.controller;

import io.pp.arcade.v1.admin.penalty.RedisPenaltyUser;
import io.pp.arcade.v1.admin.penalty.dto.PenaltyAddRequestDto;
import io.pp.arcade.v1.admin.penalty.dto.PenaltyUserResponseDto;
import io.pp.arcade.v1.admin.penalty.service.PenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PenaltyController {

    private final PenaltyService penaltyService;
    @PostMapping("pingpong/admin/users/{intraId}/penalty")
    void givePenaltyToUser(@PathVariable String intraId, @RequestBody PenaltyAddRequestDto requestDto) {
        penaltyService.givePenalty(intraId, requestDto.getPenaltyTime(), requestDto.getReason());
    }

    @GetMapping("/admin/penalty/users/{intraId}/detail")
    PenaltyUserResponseDto getPenaltyUserByIntraId(@PathVariable String intraId) {
        RedisPenaltyUser penaltyUser =  penaltyService.getOnePenaltyUser(intraId);
        return new PenaltyUserResponseDto(penaltyUser);
    }

}
