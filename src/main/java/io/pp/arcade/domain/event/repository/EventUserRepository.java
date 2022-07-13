package io.pp.arcade.domain.event.repository;

import io.pp.arcade.domain.event.entity.EventUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventUserRepository extends JpaRepository<EventUser, Integer> {
    Optional<List<EventUser>> findByEventName(String eventName);
}
