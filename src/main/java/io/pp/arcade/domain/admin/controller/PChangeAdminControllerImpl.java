package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.PChangeAllDto;
import io.pp.arcade.domain.admin.dto.create.PChangeCreateDto;
import io.pp.arcade.domain.admin.dto.delete.PChangeDeleteDto;
import io.pp.arcade.domain.admin.dto.update.PChangeUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class PChangeAdminControllerImpl implements PChangeAdminController {
    @Override
    @PostMapping(value = "/pChange")
    public void pChangeCreate(PChangeCreateDto pChangeCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/pChange/{id}")
    public void pChangeUpdate(PChangeUpdateDto pChangeUpdateDto, HttpServletRequest request) {

    }

    @Override
    @DeleteMapping(value = "/pChange/{id}")
    public void pChangeDelete(PChangeDeleteDto pChangeDeleteDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/pChange")
    public void pChangeAll(PChangeAllDto pChangeAllDto, HttpServletRequest request) {

    }
}
