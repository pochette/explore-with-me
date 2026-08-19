package ru.burdak.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.burdak.mainservice.model.Request;
import ru.burdak.mainservice.model.RequestStatus;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEvent_Id(Long eventId);

    List<Request> findAllByEvent_IdAndStatus(Long eventId, RequestStatus requestStatus);

    List<Request> findAllByIdInAndEvent_Id(Set<Long> ids, Long id);

    List<Request> findAllByRequester_Id(Long requesterId);

    List<Request> findAllByRequester_IdAndEvent_Id(Long requesterId, Long eventId);

    Optional<Request> findByRequester_Id(Long userId);

    Request findByRequester_IdAndId(Long requesterId, Long id);
}