package io.pp.arcade.v1.domain.admin.dto.update;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamUpdateDto {
    private Integer teamId;
    private String user1Id;
    private String user2Id;
    private Integer headCount;
    private Integer score;
    private Boolean win;
    private Integer teamPpp;
}
