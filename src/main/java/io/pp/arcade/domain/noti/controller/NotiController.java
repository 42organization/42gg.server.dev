package io.pp.arcade.domain.noti.controller;

import io.pp.arcade.domain.noti.dto.NotiResponseDto;
import org.springframework.web.bind.annotation.RequestParam;

public interface NotiController {
    public NotiResponseDto notiFindByUser(@RequestParam Integer userId);
}
