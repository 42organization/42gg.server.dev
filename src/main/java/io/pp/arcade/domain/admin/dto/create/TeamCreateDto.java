package io.pp.arcade.domain.admin.dto.create;

import lombok.Getter;

@Getter
public class TeamCreateDto {
    private Integer user1Id;
    private Integer user2Id;
    private Integer teamPpp;
    private Integer headCount;
    private Integer score;
    private Boolean win;
}
