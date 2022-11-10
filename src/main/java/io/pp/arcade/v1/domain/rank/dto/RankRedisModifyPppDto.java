package io.pp.arcade.v1.domain.rank.dto;

import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.GameType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RankRedisModifyPppDto {
    private UserDto userDto;
    private String statusMessage;
    private GameType gameType;
    private Integer ppp;
    private Integer modifyStatus;

    @Builder
    public RankRedisModifyPppDto(UserDto userDto, String statusMessage, GameType gameType, Integer ppp, Integer modifyStatus) {
        this.userDto = userDto;
        this.statusMessage = statusMessage;
        this.gameType = gameType;
        this.ppp = ppp;
        this.modifyStatus = modifyStatus;
    }
}
