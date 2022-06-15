package io.pp.arcade.domain.noti.controller;

import io.pp.arcade.domain.noti.NotiService;
import io.pp.arcade.domain.noti.dto.NotiDto;
import io.pp.arcade.domain.noti.dto.NotiFindDto;
import io.pp.arcade.domain.noti.dto.NotiResponseDto;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class NotiControllerImpl implements NotiController {
    private final NotiService notiService;
    private final SlotService slotService;
    private final UserService userService;

    @Override
    public NotiResponseDto notiFindByUser(Integer userId) {
        UserDto user = userService.findById(userId);
        NotiFindDto notiFindDto = NotiFindDto.builder()
                .user(user).build();
        List<NotiDto> notis = notiService.findNotiByUser(notiFindDto);

        return null;
    }

    @Override
    public void notiRemoveOne(Integer notiId) {

    }

    @Override
    public void notiRemoveAll(Integer userId) {

    }
}
