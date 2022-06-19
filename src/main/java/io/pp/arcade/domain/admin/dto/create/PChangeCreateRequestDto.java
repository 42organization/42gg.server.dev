package io.pp.arcade.domain.admin.dto.create;

import lombok.Getter;

@Getter
public class PChangeCreateRequestDto {
    private Integer pChangeId;
    private Integer gameId;
    private Integer userId;
    private String intraId;
    private Integer pppChange;
    private Integer pppResult;
}
