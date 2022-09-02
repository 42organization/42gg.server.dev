package io.pp.arcade.v1.domain.pchange.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PChangeAddDto {
    private Integer gameId;
    private Integer userId;
    private Integer pppChange;
    private Integer pppResult;
    private Integer expChange;
    private Integer expResult;

    @Override
    public String toString() {
        return "PChangeAddDto{" +
                "gameId=" + gameId +
                ", userId=" + userId +
                ", pppChange=" + pppChange +
                ", pppResult=" + pppResult +
                ", expChange=" + expChange +
                ", expResult=" + expResult +
                '}';
    }
}
