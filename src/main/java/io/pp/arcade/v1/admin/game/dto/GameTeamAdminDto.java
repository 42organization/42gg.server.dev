package io.pp.arcade.v1.admin.game.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GameTeamAdminDto {
    private String intraId1;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String intraId2;  //복식일 경우에만 있음
    private Integer teamId;
    private Integer score;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean win;
}
