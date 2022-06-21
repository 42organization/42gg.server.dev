package io.pp.arcade.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserHistoricDto {
    /*
    private Integer gameId;
    private Integer userId;
    private Integer pppChange;
    private Integer pppResult;
    private LocalDateTime time;
    */
    private Integer ppp;
    private LocalDateTime date;
}
