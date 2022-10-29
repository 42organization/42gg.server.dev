package io.pp.arcade.v1.domain.pchange.dto;

import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PChangeAddDto {
    private GameDto game;
    private UserDto user;
    private Integer pppChange;
    private Integer pppResult;
    private Integer expChange;
    private Integer expResult;

    @Override
    public String toString() {
        return "PChangeAddDto{" +
                "game=" + game +
                ", user=" + user +
                ", pppChange=" + pppChange +
                ", pppResult=" + pppResult +
                ", expChange=" + expChange +
                ", expResult=" + expResult +
                '}';
    }
}
