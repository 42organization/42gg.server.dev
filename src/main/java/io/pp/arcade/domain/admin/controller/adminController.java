package io.pp.arcade.domain.admin.controller;

import io.pp.arcade.domain.admin.dto.all.*;
import io.pp.arcade.domain.admin.dto.create.*;
import io.pp.arcade.domain.admin.dto.delete.*;
import io.pp.arcade.domain.admin.dto.update.*;
import io.pp.arcade.domain.noti.dto.NotiDeleteDto;
import io.pp.arcade.domain.season.dto.SeasonDeleteDto;

import javax.servlet.http.HttpServletRequest;

public interface adminController {
    void userCreate(UserCreateDto userCreateDto, HttpServletRequest request);
    void userUpdate(UserUpdateDto userUpdateDto, HttpServletRequest request);
    void userAll(UserAllDto userAllDto, HttpServletRequest request);

    void teamCreate(TeamCreateDto teamCreateDto, HttpServletRequest request);
    void teamUpdate(TeamUpdateDto teamUpdateDto, HttpServletRequest request);
    void teamDelete(TeamDeleteDto teamDeleteDto, HttpServletRequest request);
    void teamAll(TeamAllDto teamAllDto, HttpServletRequest request);

    void slotCreate(SlotCreateDto slotCreateDto, HttpServletRequest request);
    void slotUpdate(SlotUpdateDto slotUpdateDto, HttpServletRequest request);
    void slotDelete(SlotDeleteDto slotDeleteDto, HttpServletRequest request);
    void slotAll(SlotAllDto slotAllDto, HttpServletRequest request);

    void gameCreate(GameCreateDto gameCreateDto, HttpServletRequest request);
    void gameUpdate(GameUpdateDto gameUpdateDto, HttpServletRequest request);
    void gameDelete(GameDeleteDto gameDeleteDto, HttpServletRequest request);
    void gameAll(GameAllDto gameAllDto, HttpServletRequest request);

    void notiCreate(NotiCreateDto notiCreateDto, HttpServletRequest request);
    void notiUpdate(NotiUpdateDto notiUpdateDto, HttpServletRequest request);
    void notiDelete(NotiDeleteDto notiDeleteDto, HttpServletRequest request);
    void notiAll(NotiAllDto notiAllDto, HttpServletRequest request);

    void seasonCreate(SeasonCreateDto seasonCreateDto, HttpServletRequest request);
    void seasonUpdate(SeasonUpdateDto seasonUpdateDto, HttpServletRequest request);
    void seasonDelete(SeasonDeleteDto seasonDeleteDto, HttpServletRequest request);
    void seasonAll(SeasonAllDto seasonAllDto, HttpServletRequest request);

    void rankCreate(RankCreateDto rankCreateDto, HttpServletRequest request);
    void rankUpdate(RankUpdateDto rankUpdateDto, HttpServletRequest request);
    void rankDelete(RankDeleteDto rankDeleteDto, HttpServletRequest request);
    void rankAll(RankAllDto rankAllDto, HttpServletRequest request);

    void pChangeCreate(PChangeCreateDto pChangeCreateDto, HttpServletRequest request);
    void pChangeUpdate(PChangeUpdateDto pChangeUpdateDto, HttpServletRequest request);
    void pChangeDelete(PChangeDeleteDto pChangeDeleteDto, HttpServletRequest request);
    void pChangeAll(PChangeAllDto pChangeAllDto, HttpServletRequest request);

    void currentMatchCreate(CurrentMatchCreateDto currentMatchCreateDto, HttpServletRequest request);
    void currentMatchUpdate(CurrentMatchUpdateDto currentMatchUpdateDto, HttpServletRequest request);
    void currentMatchDelete(CurrentMatchDeleteDto currentMatchDeleteDto, HttpServletRequest request);
    void currentMatchAll(CurrentMatchAllDto currentMatchAllDto, HttpServletRequest request);
}
