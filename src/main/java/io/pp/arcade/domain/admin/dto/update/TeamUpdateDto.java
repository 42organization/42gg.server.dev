package io.pp.arcade.domain.admin.dto.update;

import lombok.Getter;

@Getter
public class TeamUpdateDto {
    private Integer id;
    private Integer user1Id;
    private Integer user2Id;
    private Integer teamPpp;
    private Integer headCount;
    private Integer score;
    private Boolean win;
}
