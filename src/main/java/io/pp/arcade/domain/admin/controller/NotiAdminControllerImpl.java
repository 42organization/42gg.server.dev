package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.NotiAllDto;
import io.pp.arcade.domain.admin.dto.create.NotiCreateDto;
import io.pp.arcade.domain.admin.dto.update.NotiUpdateDto;
import io.pp.arcade.domain.noti.dto.NotiDeleteDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class NotiAdminControllerImpl implements NotiAdminController {
    @Override
    @PostMapping(value = "/noti")
    public void notiCreate(NotiCreateDto notiCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/noti/{id}")
    public void notiUpdate(NotiUpdateDto notiUpdateDto, HttpServletRequest request) {

    }

    @Override
    @DeleteMapping(value = "/noti/{id}")
    public void notiDelete(NotiDeleteDto notiDeleteDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/noti")
    public void notiAll(NotiAllDto notiAllDto, HttpServletRequest request) {

    }
}
