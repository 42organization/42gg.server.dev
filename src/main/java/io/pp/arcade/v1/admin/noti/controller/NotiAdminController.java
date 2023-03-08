package io.pp.arcade.v1.admin.noti.controller;


import io.pp.arcade.v1.admin.noti.dto.NotiListResponseDto;
import io.pp.arcade.v1.admin.noti.dto.NotiToAllRequestDto;
import io.pp.arcade.v1.admin.noti.dto.NotiToUserRequestDto;
import io.pp.arcade.v1.admin.noti.service.NotiAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/pingpong/admin/notifications")
public class NotiAdminController {

    private final NotiAdminService notiAdminService;
    @GetMapping
    public ResponseEntity<NotiListResponseDto> getAllNotiByAdmin(@RequestParam int page,
                                                                 @RequestParam(defaultValue = "20") int size, String q) {
        if (page < 1 || size < 1) {
            return ResponseEntity.badRequest().build();
        }
        Pageable pageable = PageRequest.of(page - 1, size,
                Sort.by("createdAt").descending().and(Sort.by("user.intraId").ascending()));
        if (q == null)
            return new ResponseEntity(notiAdminService.getAllNoti(pageable), HttpStatus.OK);
        else
            return new ResponseEntity(notiAdminService.getFilteredNotifications(pageable, q), HttpStatus.OK);

    }

    @PostMapping("/{intraId}")
    public ResponseEntity addNoitToUserByAdmin(@PathVariable String intraId,
                                               @RequestBody NotiToUserRequestDto requestDto) {
        try{
            notiAdminService.addNotiToUser(intraId, requestDto.getMessage());
        } catch (NoSuchElementException e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public void addNotiToAllUser(@RequestBody NotiToAllRequestDto requestDto) {
        notiAdminService.addNotiToAll(requestDto.getMessage());
    }

}
