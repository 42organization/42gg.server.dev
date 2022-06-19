package io.pp.arcade.domain.slot.controller;

import io.pp.arcade.domain.slot.dto.SlotAddUserRequestDto;
import io.pp.arcade.domain.slot.dto.SlotStatusResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

public interface SlotController {
    SlotStatusResponseDto slotStatusList(@PathVariable Integer tableId, @RequestParam String type, HttpServletRequest request);
    void slotAddUser(@PathVariable Integer tableId, @RequestBody SlotAddUserRequestDto addReqDto, HttpServletRequest request) throws MessagingException;
    void slotRemoveUser(@PathVariable Integer tableId, @RequestParam Integer slotId, HttpServletRequest request) throws MessagingException;
}
