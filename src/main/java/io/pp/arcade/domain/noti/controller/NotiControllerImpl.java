package io.pp.arcade.domain.noti.controller;

import io.pp.arcade.domain.noti.NotiService;
import io.pp.arcade.domain.noti.dto.*;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.team.TeamService;
import io.pp.arcade.domain.team.dto.TeamPosDto;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserService;
import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserFindDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class NotiControllerImpl implements NotiController {
    private final NotiService notiService;
    private final TeamService teamService;
    private final UserService userService;

    @Override
    @GetMapping(value = "/notifications")
    public NotiResponseDto notiFindByUser(Integer userId) {
        UserDto user = userService.findById(UserFindDto.builder().userId(userId).build());
        NotiFindDto notiFindDto = NotiFindDto.builder()
                .user(user).build();
        List<NotiDto> notis = notiService.findNotiByUser(notiFindDto);
        List<Object> notiDtos = new ArrayList<>();
        notis.forEach(noti -> {
            if (noti.getType().equals("announce")) {
                notiDtos.add(NotiAnnounceDto.builder()
                        .id(noti.getId())
                        .type(noti.getType())
                        .isChecked(noti.getIsChecked())
                        .message(noti.getMessage())
                        .createdTime(noti.getCreatdDate())
                        .build());
            } else if (noti.getType().equals("matched")) {
                notiDtos.add(NotiMatchedDto.builder()
                        .id(noti.getId())
                        .type(noti.getType())
                        .time(noti.getSlot().getTime())
                        .isChecked(noti.getIsChecked())
                        .createdTime(noti.getCreatdDate())
                        .build());
            } else if (noti.getType().equals("imminent")) {
                TeamPosDto teamPosDto = teamService.getTeamPosNT(user, noti.getSlot().getTeam1(), noti.getSlot().getTeam2());
                List<String> myTeam = new ArrayList<>();
                List<String> enemyTeam = new ArrayList<>();
                if (teamPosDto.getMyTeam().getUser1() != null) {
                    myTeam.add(teamPosDto.getMyTeam().getUser1().getIntraId());
                }
                if (teamPosDto.getMyTeam().getUser2() != null) {
                    myTeam.add(teamPosDto.getMyTeam().getUser2().getIntraId());
                }
                if (teamPosDto.getEnemyTeam().getUser1() != null) {
                    enemyTeam.add(teamPosDto.getEnemyTeam().getUser1().getIntraId());
                }
                if (teamPosDto.getEnemyTeam().getUser2() != null) {
                    enemyTeam.add(teamPosDto.getEnemyTeam().getUser2().getIntraId());
                }
                notiDtos.add(NotiImminentMatchDto.builder()
                        .id(noti.getId())
                        .type(noti.getType())
                        .time(noti.getSlot().getTime())
                        .isChecked(noti.getIsChecked())
                        .myTeam(myTeam)
                        .enemyTeam(enemyTeam)
                        .createdTime(noti.getCreatdDate())
                        .build());
            } else if (noti.getType().equals("canceled")) {
                notiDtos.add(NotiCanceledDto.builder()
                        .id(noti.getId())
                        .type(noti.getType())
                        .time(noti.getSlot().getTime())
                        .isChecked(noti.getIsChecked())
                        .createdTime(noti.getCreatdDate())
                        .build());
            }
        });
        NotiModifyDto modifyDto = NotiModifyDto.builder()
                .user(user).build();
        notiService.modifyNotiChecked(modifyDto);
        NotiResponseDto responseDto = NotiResponseDto.builder()
                .notifications(notiDtos)
                .build();
        return responseDto;
    }

    @Override
    @DeleteMapping(value = "notifications/{notiId}")
    public void notiRemoveOne(Integer notiId) {
        NotiDeleteDto deleteDto = NotiDeleteDto.builder()
                .notiId(notiId)
                .build();
        notiService.removeNotiById(deleteDto);
    }

    @Override
    @DeleteMapping(value = "/notifications")
    public void notiRemoveAll(Integer userId) {
        UserDto user = userService.findById(UserFindDto.builder().userId(userId).build());
        NotiDeleteDto deleteDto = NotiDeleteDto.builder()
                        .user(user)
                        .build();
        notiService.removeAllNotisByUser(deleteDto);
    }
}
