package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.*;
import io.pp.arcade.domain.admin.dto.create.*;
import io.pp.arcade.domain.admin.dto.delete.*;
import io.pp.arcade.domain.admin.dto.update.*;
import io.pp.arcade.domain.currentmatch.CurrentMatchService;
import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameService;
import io.pp.arcade.domain.noti.NotiService;
import io.pp.arcade.domain.noti.dto.NotiDeleteDto;
import io.pp.arcade.domain.pchange.PChangeService;
import io.pp.arcade.domain.rank.RankService;
import io.pp.arcade.domain.season.SeasonService;
import io.pp.arcade.domain.season.dto.SeasonDeleteDto;
import io.pp.arcade.domain.slot.SlotService;
import io.pp.arcade.domain.team.TeamService;
import io.pp.arcade.domain.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class adminControllerImpl implements adminController {
    private final UserService userService;
    private final TeamService teamService;
    private final SlotService slotService;
    private final GameService gameService;
    private final NotiService notiService;
    private final SeasonService seasonService;
    private final RankService rankService;
    private final PChangeService pChangeService;
    private final CurrentMatchService currentMatchService;

    @Override
    @PostMapping(value = "/user")
    public void userCreate(UserCreateDto userCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/user/{id}")
    public void userUpdate(UserUpdateDto userUpdateDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/user")
    public void userAll(UserAllDto userAllDto, HttpServletRequest request) {

    }

    @Override
    @PostMapping(value = "/team")
    public void teamCreate(TeamCreateDto teamCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/team/{id}")
    public void teamUpdate(TeamUpdateDto teamUpdateDto, HttpServletRequest request) {

    }

    @Override
    @DeleteMapping(value = "/team/{id}")
    public void teamDelete(TeamDeleteDto teamDeleteDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/team")
    public void teamAll(TeamAllDto teamAllDto, HttpServletRequest request) {

    }

    @Override
    @PostMapping(value = "/slot")
    public void slotCreate(SlotCreateDto slotCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/slot/{id}")
    public void slotUpdate(SlotUpdateDto slotUpdateDto, HttpServletRequest request) {

    }

    @Override
    @DeleteMapping(value = "/slot/{id}")
    public void slotDelete(SlotDeleteDto slotDeleteDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/slot")
    public void slotAll(SlotAllDto slotAllDto, HttpServletRequest request) {

    }

    @Override
    @PostMapping(value = "/game")
    public void gameCreate(GameCreateDto gameCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/game/{id}")
    public void gameUpdate(GameUpdateDto gameUpdateDto, HttpServletRequest request) {

    }

    @Override
    @DeleteMapping(value = "/game/{id}")
    public void gameDelete(GameDeleteDto gameDeleteDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/game")
    public void gameAll(GameAllDto gameAllDto, HttpServletRequest request) {

    }

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

    @Override
    @PostMapping(value = "/season")
    public void seasonCreate(SeasonCreateDto seasonCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/season/{id}")
    public void seasonUpdate(SeasonUpdateDto seasonUpdateDto, HttpServletRequest request) {

    }

    @Override
    @DeleteMapping(value = "/season/{id}")
    public void seasonDelete(SeasonDeleteDto seasonDeleteDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/season")
    public void seasonAll(SeasonAllDto seasonAllDto, HttpServletRequest request) {

    }

    @Override
    @PostMapping(value = "/rank")
    public void rankCreate(RankCreateDto rankCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/rank/{id}")
    public void rankUpdate(RankUpdateDto rankUpdateDto, HttpServletRequest request) {

    }

    @Override
    @DeleteMapping(value = "/rank/{id}")
    public void rankDelete(RankDeleteDto rankDeleteDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/rank")
    public void rankAll(RankAllDto rankAllDto, HttpServletRequest request) {

    }

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

    @Override
    @PostMapping(value = "/currentMatch")
    public void currentMatchCreate(CurrentMatchCreateDto currentMatchCreateDto, HttpServletRequest request) {

    }

    @Override
    @PutMapping(value = "/currentMatch/{id}")
    public void currentMatchUpdate(CurrentMatchUpdateDto currentMatchUpdateDto, HttpServletRequest request) {

    }

    @Override
    @DeleteMapping(value = "/currentMatch/{id}")
    public void currentMatchDelete(CurrentMatchDeleteDto currentMatchDeleteDto, HttpServletRequest request) {

    }

    @Override
    @GetMapping(value = "/currentMatch")
    public void currentMatchAll(CurrentMatchAllDto currentMatchAllDto, HttpServletRequest request) {

    }
}
