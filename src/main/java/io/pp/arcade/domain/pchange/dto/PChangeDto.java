package io.pp.arcade.domain.pchange.dto;

import io.pp.arcade.domain.game.dto.GameDto;
import io.pp.arcade.domain.pchange.PChange;
import io.pp.arcade.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PChangeDto {
    private GameDto game;
    private UserDto user;
    private Integer pppChange;
    private Integer pppResult;

    public static PChangeDto from(PChange pChange){
        return PChangeDto.builder()
                .game(GameDto.from(pChange.getGame()))
                .user(UserDto.from(pChange.getUser()))
                .pppChange(pChange.getPppChange())
                .pppResult(pChange.getPppResult())
                .build();
    }
}
