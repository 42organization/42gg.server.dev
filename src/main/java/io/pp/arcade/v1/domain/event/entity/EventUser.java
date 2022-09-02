package io.pp.arcade.v1.domain.event.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class EventUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "intra_id")
    private String intraId;

    @Column(name = "event_name")
    private String eventName;

    @Builder
    public EventUser(String intraId, String eventName) {
        this.intraId = intraId;
        this.eventName = eventName;
    }
}
