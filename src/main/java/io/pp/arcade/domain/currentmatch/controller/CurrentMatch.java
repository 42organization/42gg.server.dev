package io.pp.arcade.domain.currentmatch.controller;

import org.springframework.web.bind.annotation.RequestParam;

public interface CurrentMatch {
    void CurrentMatchFind(@RequestParam Integer userId);
}
