package io.pp.arcade.v1.domain.game.controller;

import io.pp.arcade.v1.domain.game.dto.*;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

public interface GameController {
    //게임 결과 정보 - GET /pingpong/games/result
    GameUserInfoResponseDto gameUserInfo(HttpServletRequest request);
    //게임 결과 입력 - POST /pingpong/games/result
    void gameResultSave(@RequestBody @Valid GameResultRequestDto getterDto, HttpServletRequest request) throws MessagingException;

    GameResultResponseDto gameResultByUserIdAndByGameIdAndCount(@PathVariable String intraId, @ModelAttribute @Valid GameResultUserPageRequestDto requestDto, HttpServletRequest request);
}
