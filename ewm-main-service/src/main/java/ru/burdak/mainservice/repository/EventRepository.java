package ru.burdak.mainservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.burdak.mainservice.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    boolean existsByCategory_Id(Long categoryId);

    List<Event> findAllByInitiator_Id(Long initiatorId);

    List<Event> findAllByInitiator_Id(Long initiatorId, Pageable pageable);

    Optional<Event> findByInitiator_IdAndId(Long initiatorId, Long id);

    Long id(Long id);
}
