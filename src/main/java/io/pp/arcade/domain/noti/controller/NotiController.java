package io.pp.arcade.domain.noti.controller;

import io.pp.arcade.domain.noti.dto.NotiResponseDto;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

public interface NotiController {
    NotiResponseDto notiFindByUser(HttpServletRequest request);
    void notiRemoveOne(@PathVariable  Integer notiId, HttpServletRequest request);
    void notiRemoveAll(HttpServletRequest request);
}
