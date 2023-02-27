package io.pp.arcade.v1.admin.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GameLogListAdminResponseDto {
    private List<GameLogAdminDto> gameLogList;
    private int totalPage;
    private int currentPage;

}
