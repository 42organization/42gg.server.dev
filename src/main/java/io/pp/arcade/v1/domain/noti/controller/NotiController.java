package io.pp.arcade.v1.domain.noti.controller;

import io.pp.arcade.v1.domain.noti.dto.NotiResponseDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;

@Validated
public interface NotiController {
    NotiResponseDto notiFindByUser(HttpServletRequest request);
    void notiRemoveOne(@PathVariable @Positive Integer notiId, HttpServletRequest request);
    void notiRemoveAll(HttpServletRequest request);
}
