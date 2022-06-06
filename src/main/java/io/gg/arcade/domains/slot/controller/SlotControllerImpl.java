package io.gg.arcade.domains.slot.controller;


import io.gg.arcade.domains.slot.dto.SlotResponseDto;
import io.gg.arcade.domains.slot.service.SlotService;
import io.gg.arcade.domains.slot.service.SlotServiceImpl;

public class SlotControllerImpl implements SlotController {
    private final SlotService slotService = new SlotServiceImpl();
    @Override
    // 매칭 테이블 조회 - GET /pingpong/match/tables/{tableId}?type=type
    public SlotResponseDto slotList(Integer tableId, String type) {
         slotService.findAll();
         return null;
    }

    // 매칭 등록 - POST /pingpong/match/tables/{tableId}
    @Override
    public SlotResponseDto slotSave(Integer tableId) {
        return null;
    }

    @Override
    // 매칭 취소 - DELETE /pingpong/match/tables/{tableId}?matchId={matchId}
    public SlotResponseDto slotModified(Integer tableId, Integer matchId) {
        return null;
    }
}
