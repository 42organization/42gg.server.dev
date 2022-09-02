package io.pp.arcade.v1.domain.event.repository;

import io.pp.arcade.v1.domain.event.entity.PingPongEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PingPongEventRepository extends JpaRepository<PingPongEvent, Integer> {
    Optional<List<PingPongEvent>> findByCurrentEvent(Boolean currentEvent);
}

