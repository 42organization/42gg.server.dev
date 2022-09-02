package io.pp.arcade.v1.domain.admin.dto.update;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TeamUpdateRequestDto {
    private Integer teamId;
    private String user1Id;
    private String user2Id;
    private Integer teamPpp;
    private Integer headCount;
    private Integer score;
    private Boolean win;
}
