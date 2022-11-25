package io.pp.arcade.v1.domain.opponent.controller;

import io.pp.arcade.v1.domain.opponent.dto.OpponentResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OpponentController {
    List<OpponentResponseDto> findOpponentList(HttpServletRequest request);
}
