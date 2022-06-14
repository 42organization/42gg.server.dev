package io.pp.arcade.domain.currentmatch.controller;

import io.pp.arcade.domain.currentmatch.dto.CurrentMatchResponseDto;
import org.springframework.web.bind.annotation.RequestParam;

public interface CurrentMatchController {
    CurrentMatchResponseDto currentMatchFind(@RequestParam Integer userId);
}
