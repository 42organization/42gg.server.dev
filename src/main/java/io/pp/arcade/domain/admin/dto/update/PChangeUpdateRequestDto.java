package io.pp.arcade.domain.admin.dto.update;

import lombok.Getter;

@Getter
public class PChangeUpdateRequestDto {
    private Integer pChangeId;
    private Integer gameId;
    private Integer userId; //userId랑 intraId 중에 뭘 남겨야 할지 모르겠다
    private String intraId;
    private Integer pppChange;
    private Integer pppResult;
}
