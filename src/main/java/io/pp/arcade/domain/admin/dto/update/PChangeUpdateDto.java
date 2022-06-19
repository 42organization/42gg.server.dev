package io.pp.arcade.domain.admin.dto.update;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PChangeUpdateDto {
    private Integer pChangeId;
    private Integer gameId;
    private Integer userId;
    private String intraId;
    private Integer pppChange;
    private Integer pppResult;
}
