package io.pp.arcade.v1.domain.event.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveEventDto {
    String eventName;
    String content;
}
