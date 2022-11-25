package io.pp.arcade.v1.domain.admin.controller;

import io.pp.arcade.v1.domain.admin.dto.GameAddRequestDto;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface GuestGameGeneratorAdminController {
    void gameAdd(@RequestBody GameAddRequestDto gameAddRequestDto, HttpServletRequest request);
}
