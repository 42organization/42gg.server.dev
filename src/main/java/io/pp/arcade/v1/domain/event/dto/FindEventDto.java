package io.pp.arcade.v1.domain.event.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindEventDto {
    private String eventName;
    private String intraId;
}
