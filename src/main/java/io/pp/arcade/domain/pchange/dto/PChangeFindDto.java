package io.pp.arcade.domain.pchange.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

@Builder
@Getter
@ToString
public class PChangeFindDto {
    private Integer gameId;
    private String userId;
    private Pageable pageable;
}
