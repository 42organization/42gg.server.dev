package io.pp.arcade.v1.domain.event.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class PingPongEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "content")
    private String content;

    @Column(name = "current_event")
    private Boolean currentEvent;

    @Builder
    public PingPongEvent(Integer id, String eventName, String content, Boolean currentEvent) {
        this.id = id;
        this.eventName = eventName;
        this.content = content;
        this.currentEvent = currentEvent;
    }
}
