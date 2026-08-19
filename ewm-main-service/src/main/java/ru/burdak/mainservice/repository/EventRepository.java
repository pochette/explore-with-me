package ru.burdak.mainservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.burdak.mainservice.model.Event;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    boolean existsByCategory_Id(Long categoryId);

    Set<Event> findAllByIdIn(Collection<Long> ids);

    List<Event> findAllByInitiator_Id(Long initiatorId, Pageable pageable);

    Optional<Event> findByInitiator_IdAndId(Long initiatorId, Long id);

}
