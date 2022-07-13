package io.pp.arcade.domain.event.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveEventDto {
    String eventName;
    String content;
}
