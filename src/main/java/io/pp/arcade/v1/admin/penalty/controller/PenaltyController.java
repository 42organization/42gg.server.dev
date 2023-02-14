package io.pp.arcade.v1.admin.penalty.controller;

import io.pp.arcade.v1.admin.penalty.RedisPenaltyUser;
import io.pp.arcade.v1.admin.penalty.dto.PenaltyAddRequestDto;
import io.pp.arcade.v1.admin.penalty.dto.PenaltyListResponseDto;
import io.pp.arcade.v1.admin.penalty.dto.PenaltyUserResponseDto;
import io.pp.arcade.v1.admin.penalty.service.PenaltyService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("pingpong/admin/")
public class PenaltyController {

    private final PenaltyService penaltyService;
    @PostMapping("users/{intraId}/penalty")
    public ResponseEntity givePenaltyToUser(@PathVariable String intraId, @RequestBody PenaltyAddRequestDto requestDto) {
        try {
            penaltyService.givePenalty(intraId, requestDto.getPenaltyTime(), requestDto.getReason());
        } catch (NoSuchElementException e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("penalty/users/{intraId}/detail")
    public PenaltyUserResponseDto getPenaltyUserByIntraId(@PathVariable String intraId) {
        RedisPenaltyUser penaltyUser =  penaltyService.getOnePenaltyUser(intraId);
        if (penaltyUser != null)
            return new PenaltyUserResponseDto(penaltyUser);
        else
            return null;
    }

    @GetMapping("penalty/users")
    public PenaltyListResponseDto getAllPenaltyUser(String q, int page, HttpResponse httpResponse,
                                                    @RequestParam(defaultValue = "20") int size) {
        if (page < 1 || size < 1) {
            httpResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            return null;
        }
        if (q == null)
            return penaltyService.getAllPenaltyUser(page - 1, size);
        else
            return penaltyService.searchPenaltyUser(q, page -1, size);

    }

    @DeleteMapping("penalty/users/{intraId}")
    public void releasePenaltyUser(@PathVariable String intraId) {
        penaltyService.releasePenaltyUser(intraId);
    }

}
