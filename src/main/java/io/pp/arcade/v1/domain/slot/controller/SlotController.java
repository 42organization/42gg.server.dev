package io.pp.arcade.v1.domain.slot.controller;

import io.pp.arcade.v1.domain.slot.dto.SlotAddUserRequestDto;
import io.pp.arcade.v1.domain.slot.dto.SlotStatusResponseDto;
import io.pp.arcade.v1.global.type.GameType;
import io.pp.arcade.v1.global.type.Mode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

public interface SlotController {
    SlotStatusResponseDto slotStatusList(@PathVariable Integer tableId, @PathVariable Mode mode, @PathVariable GameType type, HttpServletRequest request);
    void slotAddUser(@PathVariable Integer tableId, @PathVariable GameType type, @RequestBody @Valid SlotAddUserRequestDto addReqDto, HttpServletRequest request) throws MessagingException;
    void slotRemoveUser(@PathVariable Integer slotId, HttpServletRequest request) throws MessagingException;
//    SlotFindOpponentResDto FindOpponentAvailable();
}
