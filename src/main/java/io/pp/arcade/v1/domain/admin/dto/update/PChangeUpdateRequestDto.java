package io.pp.arcade.v1.domain.admin.dto.update;

import lombok.Getter;

@Getter
public class PChangeUpdateRequestDto {
    private Integer pchangeId;
    private Integer gameId;
    private String userId;
    private Integer pppChange;
    private Integer pppResult;
}
