package io.pp.arcade.domain.game.controller;

import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.game.dto.GameResultRequestDto;
import io.pp.arcade.domain.game.dto.GameResultResponseDto;
import io.pp.arcade.domain.game.dto.GameUserInfoResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GameController {
    //게임 결과 정보 - GET /pingpong/games/{gameId}/result
    GameUserInfoResponseDto gameUserInfo(@PathVariable Integer gameId, @RequestParam Integer userId);
    //게임 결과 입력 - POST /pingpong/games/{gameId}/result
    void gameResultSave(@PathVariable Integer gameId, @RequestBody GameResultRequestDto getterDto, @RequestParam Integer userId);
    GameResultResponseDto gameResultByIndexAndCount(@RequestParam(required = false) Integer index, @RequestParam(required = false) Integer count, @RequestParam(required = false) String status);
    GameResultResponseDto gameResultByUserIdAndIndexAndCount(@PathVariable Integer userId, @RequestParam Integer index, @RequestParam Integer count, @RequestParam String type);
}
