package io.pp.arcade.v1.domain.event.dto;

import io.pp.arcade.v1.domain.event.entity.EventUser;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventUserDto {
    private Integer id;
    private String intraId;
    private String eventName;

    public static EventUserDto from(EventUser eventUser) {
        return EventUserDto.builder()
                .id(eventUser.getId())
                .intraId(eventUser.getIntraId())
                .eventName(eventUser.getEventName())
                .build();
    }
}
