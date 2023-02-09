package io.pp.arcade.v1.admin.penalty.controller;

import io.pp.arcade.v1.admin.penalty.RedisPenaltyUser;
import io.pp.arcade.v1.admin.penalty.dto.PenaltyAddRequestDto;
import io.pp.arcade.v1.admin.penalty.dto.PenaltyUserResponseDto;
import io.pp.arcade.v1.admin.penalty.service.PenaltyService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("pingpong/admin/")
public class PenaltyController {

    private final PenaltyService penaltyService;
    @PostMapping("users/{intraId}/penalty")
    ResponseEntity givePenaltyToUser(@PathVariable String intraId, @RequestBody PenaltyAddRequestDto requestDto) {
        try {
            penaltyService.givePenalty(intraId, requestDto.getPenaltyTime(), requestDto.getReason());
        }catch (NoSuchElementException e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("penalty/users/{intraId}/detail")
    PenaltyUserResponseDto getPenaltyUserByIntraId(@PathVariable String intraId) {
        RedisPenaltyUser penaltyUser =  penaltyService.getOnePenaltyUser(intraId);
        if (penaltyUser != null)
            return new PenaltyUserResponseDto(penaltyUser);
        else
            return null;
    }

    @GetMapping("penalty/users")
    List<PenaltyUserResponseDto> getAllPenaltyUser() {
        return penaltyService.getAllPenaltyUser();
    }

    @DeleteMapping("penalty/users/{intraId}")
    void releasePenaltyUser(@PathVariable String intraId) {
        penaltyService.releasePenaltyUser(intraId);
    }

    @GetMapping("penalty/users/{intraId}")
    List<PenaltyUserResponseDto> searchPenaltyUser(@PathVariable String intraId) {
        return penaltyService.searchPenaltyUser(intraId);
    }

}
