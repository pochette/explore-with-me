package ru.burdak.mainservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.burdak.mainservice.model.Event;
import ru.burdak.mainservice.model.EventState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    boolean existsByCategory_Id(Long categoryId);

    List<Event> findAllByInitiator_Id(Long initiatorId);

    List<Event> findAllByInitiator_Id(Long initiatorId, Pageable pageable);

    List<Event> findAllByInitiator_IdInAndStateInAndCategory_IdInAndEventDateBetween(Collection<Long> initiatorIds, Collection<EventState> states, Collection<Long> categoryIds, LocalDateTime eventDateAfter,
                                                                                     LocalDateTime eventDateBefore, Pageable pageable);

    Optional<Event> findByInitiator_IdAndId(Long initiatorId, Long id);

    Long id(Long id);
}
