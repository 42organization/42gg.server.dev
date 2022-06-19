package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.NotiAllDto;
import io.pp.arcade.domain.admin.dto.create.NotiCreateRequestDto;
import io.pp.arcade.domain.admin.dto.update.NotiUpdateRequestDto;
import io.pp.arcade.domain.noti.NotiService;
import io.pp.arcade.domain.noti.dto.NotiDeleteDto;
import io.pp.arcade.domain.noti.dto.NotiDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class NotiAdminControllerImpl implements NotiAdminController {
    private final NotiService notiService;

    @Override
    @PostMapping(value = "/noti")
    public void notiCreate(NotiCreateRequestDto createRequestDto, HttpServletRequest request) {
        notiService.createNotiByAdmin(createRequestDto);
    }

    @Override
    @PutMapping(value = "/noti")
    public void notiUpdate(NotiUpdateRequestDto updateRequestDto, HttpServletRequest request) {
        notiService.updateNotiByAdmin(updateRequestDto);
    }

    @Override
    @DeleteMapping(value = "/noti/{id}")
    public void notiDelete(Integer notiId, HttpServletRequest request) {
        NotiDeleteDto notiDeleteDto = NotiDeleteDto.builder().notiId(notiId).build();
        notiService.deleteNotibyAdmin(notiDeleteDto);
    }

    @GetMapping(value = "/noti")
    public List<NotiDto> notiAll(Pageable pageable, HttpServletRequest request) {
        List<NotiDto> notis = notiService.findNotiByAdmin(pageable);
        return notis;
    }
}
