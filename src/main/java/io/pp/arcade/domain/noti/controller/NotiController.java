package io.pp.arcade.domain.noti.controller;

import io.pp.arcade.domain.noti.dto.NotiResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface NotiController {
    NotiResponseDto notiFindByUser(@RequestParam Integer userId);
    void notiRemoveOne(@PathVariable Integer notiId);
    void notiRemoveAll(@RequestParam Integer userId);
}
