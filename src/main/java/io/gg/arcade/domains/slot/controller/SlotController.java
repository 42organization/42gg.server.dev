package io.gg.arcade.domains.slot.controller;

import io.gg.arcade.domains.slot.dto.SlotResponseDto;

public interface SlotController {
    // 매칭 테이블 조회 - GET /pingpong/match/tables/{tableId}?type=type
    SlotResponseDto slotList(Integer tableId, String type);
    // 매칭 등록 - POST /pingpong/match/tables/{tableId}
    SlotResponseDto slotSave(Integer tableId);
    // 매칭 취소 - DELETE /pingpong/match/tables/{tableId}?matchId={matchId}
    SlotResponseDto slotModified(Integer tableId, Integer matchId);
}
