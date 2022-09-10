package io.pp.arcade.v1.domain.pchange.dto;

import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Builder
@Getter
public class PChangeFindDto {
    private Integer gameId;
    private String userId;
    private Integer season;
    private Mode mode;
    private Pageable pageable;

    @Override
    public String toString() {
        return "PChangeFindDto{" +
                "gameId=" + gameId +
                ", userId='" + userId + '\'' +
                ", season='" + season + '\'' +
                ", mode=" + mode +
                ", pageable=" + pageable +
                '}';
    }
}
