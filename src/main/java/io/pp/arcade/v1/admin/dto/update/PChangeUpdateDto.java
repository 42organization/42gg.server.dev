package io.pp.arcade.v1.admin.dto.update;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PChangeUpdateDto {
    private Integer gameId;
    private String userId;
    private Integer pppChange;
    private Integer pppResult;
}
