package io.pp.arcade.domain.game.controller;

import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.game.dto.GameResultRequestDto;
import io.pp.arcade.domain.game.dto.GameResultResponseDto;
import io.pp.arcade.domain.game.dto.GameUserInfoResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GameController {
    //게임 결과 정보 - GET /pingpong/games/{gameId}/result
    GameUserInfoResponseDto gameUserInfo(@PathVariable Integer gameId, @RequestParam Integer userId);
    //게임 결과 입력 - POST /pingpong/games/{gameId}/result
    void gameResultSave(@PathVariable Integer gameId, @RequestBody GameResultRequestDto getterDto, @RequestParam Integer userId);
    GameResultResponseDto gameResultByGameIdAndCount(@RequestParam(required = true) Integer count, @RequestParam(required = false) Integer gameId, @RequestParam(required = false) String status);
    GameResultResponseDto gameResultByUserIdAndByGameIdAndCount(@PathVariable String userId, @RequestParam(required = true) Integer count, @RequestParam(required = false) Integer gameId, @RequestParam(required = false) String type);

}
