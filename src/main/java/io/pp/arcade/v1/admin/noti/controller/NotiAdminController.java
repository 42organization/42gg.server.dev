package io.pp.arcade.v1.admin.noti.controller;


import io.pp.arcade.v1.admin.noti.dto.NotiAllResponseDto;
import io.pp.arcade.v1.admin.noti.dto.NotiToAllRequestDto;
import io.pp.arcade.v1.admin.noti.dto.NotiToUserRequestDto;
import io.pp.arcade.v1.admin.noti.service.NotiAdminService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notifications")
public class NotiAdminController {

    private final NotiAdminService notiAdminService;
    @GetMapping
    public NotiAllResponseDto getAllNotiByAdmin(@RequestParam int page,
                                                @RequestParam(defaultValue = "20") int size, HttpResponse httpResponse) {
        if (page < 1 || size < 1) {
            httpResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            return null;
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        return notiAdminService.getAllNoti(pageable);
    }

    @PostMapping("/{intraId}")
    public void addNoitToUserByAdmin(@PathVariable String intraId,
                                     @RequestBody NotiToUserRequestDto requestDto) throws MessagingException {
        notiAdminService.addNotiToUser(intraId, requestDto.getSlotId(),
                                requestDto.getType(), requestDto.getMessage(), requestDto.getSendMail());
    }

    @PostMapping()
    public void addNotiToAllUser(@RequestBody NotiToAllRequestDto requestDto) {
        notiAdminService.addNotiToAll(requestDto.getMessage(), requestDto.getType(), requestDto.getSendMail());
    }

}
