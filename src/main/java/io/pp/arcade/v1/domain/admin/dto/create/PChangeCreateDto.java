package io.pp.arcade.v1.domain.admin.dto.create;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PChangeCreateDto {
    private Integer pChangeId;
    private Integer pppChange;
    private Integer pppResult;
}
