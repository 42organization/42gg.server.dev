package io.pp.arcade.v1.domain.pchange.dto;

import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Builder
@Getter
public class PChangeFindDto {
    private GameDto game;
    private UserDto user;
    private Integer season;
    private Mode mode;
    private Integer count;

    @Override
    public String toString() {
        return "PChangeFindDto{" +
                "game=" + game +
                ", user='" + user + '\'' +
                ", season='" + season + '\'' +
                ", mode=" + mode +
                ", count=" + count +
                '}';
    }
}
