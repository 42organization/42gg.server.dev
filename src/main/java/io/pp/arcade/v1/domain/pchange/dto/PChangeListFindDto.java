package io.pp.arcade.v1.domain.pchange.dto;

import io.pp.arcade.v1.global.type.Mode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@Builder
public class PChangeListFindDto {
    private String intraId;
    private Integer season;
    private Integer gameId;
    private Mode mode;
    private Integer count;
}
