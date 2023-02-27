package io.pp.arcade.v1.domain.slot;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    List<Slot> findAllByTimeAfterOrderByTimeAsc(LocalDateTime time);
    Optional<Slot> findByTime(LocalDateTime time);
    Page<Slot> findAllByOrderByIdDesc(Pageable pageable);
    Optional<Slot> findFirstByOrderByTimeDesc();
}
