package io.pp.arcade.domain.season;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.crypto.SealedObject;
import java.time.LocalDateTime;
import java.util.Optional;

public interface SeasonRepository extends JpaRepository <Season, Integer> {
    Optional<Season> findByStartTimeAfterAndEndTimeBefore(LocalDateTime currentTime);
}
