package io.pp.arcade.domain.event.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindEventDto {
    private String eventName;
    private String intraId;
}
