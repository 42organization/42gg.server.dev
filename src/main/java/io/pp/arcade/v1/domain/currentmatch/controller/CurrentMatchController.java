package io.pp.arcade.v1.domain.currentmatch.controller;

import io.pp.arcade.v1.domain.currentmatch.dto.CurrentMatchResponseDto;

import javax.servlet.http.HttpServletRequest;

public interface CurrentMatchController {
    CurrentMatchResponseDto currentMatchFind(HttpServletRequest request);
}
