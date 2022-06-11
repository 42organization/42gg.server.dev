package io.pp.arcade.domain.currentmatch.controller;

import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.currentmatch.dto.CurrentMatchDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class CurrentMatchImpl implements CurrentMatch {
    private final CurrentMatchService currentMatchService;

    @Override
    @GetMapping(value = "/match/current")
    public void CurrentMatchFind(Integer userId) {
        CurrentMatchDto currentMatch = currentMatchService.findCurrentMatchByUserId(userId);
    }
}
