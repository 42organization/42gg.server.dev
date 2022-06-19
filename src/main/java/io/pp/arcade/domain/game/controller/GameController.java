package io.pp.arcade.domain.game.controller;

import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.game.dto.GameResultRequestDto;
import io.pp.arcade.domain.game.dto.GameResultResponseDto;
import io.pp.arcade.domain.game.dto.GameUserInfoResponseDto;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.StatusType;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface GameController {
    //게임 결과 정보 - GET /pingpong/games/{gameId}/result
    GameUserInfoResponseDto gameUserInfo(HttpServletRequest request);
    //게임 결과 입력 - POST /pingpong/games/{gameId}/result
    void gameResultSave(@RequestBody GameResultRequestDto getterDto, HttpServletRequest request);
    GameResultResponseDto gameResultByGameIdAndCount(@RequestParam(required = true) Integer count, @RequestParam(required = false) Integer gameId, @RequestParam(required = false) StatusType status);
    GameResultResponseDto gameResultByUserIdAndByGameIdAndCount(@PathVariable String userId, @RequestParam(required = true) Integer count, @RequestParam(required = false) Integer gameId, @RequestParam(required = false) GameType type);

}
