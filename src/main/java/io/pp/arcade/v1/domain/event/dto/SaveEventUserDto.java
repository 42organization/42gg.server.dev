package io.pp.arcade.v1.domain.event.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveEventUserDto {
    private String intraId;
    private String eventName;
}
