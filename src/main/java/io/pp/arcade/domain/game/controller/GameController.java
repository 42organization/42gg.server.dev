package io.pp.arcade.domain.game.controller;

import io.pp.arcade.domain.game.dto.*;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.Mode;
import io.pp.arcade.global.type.StatusType;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

public interface GameController {
    //게임 결과 정보 - GET /pingpong/games/result
    GameUserInfoResponseDto gameUserInfo(HttpServletRequest request);
    //게임 결과 입력 - POST /pingpong/games/result
    void gameResultSave(@RequestBody @Valid GameResultRequestDto getterDto, HttpServletRequest request) throws MessagingException;
    GameResultResponseDto gameResultByGameIdAndCount(@ModelAttribute @Valid GameResultPageRequestDto requestDto, HttpServletRequest request);

    GameResultResponseDto gameResultByGameIdAndCount(@PathVariable Mode mode, GameResultPageRequestDto requestDto, HttpServletRequest request);

    GameResultResponseDto gameResultByUserIdAndByGameIdAndCount(@PathVariable String intraId, @ModelAttribute @Valid GameResultUserPageRequestDto requestDto, HttpServletRequest request);

}
