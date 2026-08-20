package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.burdak.mainservice.dto.request.ParticipationRequestDto;
import ru.burdak.mainservice.exception.ConditionsNotMetException;
import ru.burdak.mainservice.exception.ConflictException;
import ru.burdak.mainservice.exception.NotFoundException;
import ru.burdak.mainservice.mapper.RequestMapper;
import ru.burdak.mainservice.model.*;
import ru.burdak.mainservice.repository.EventRepository;
import ru.burdak.mainservice.repository.RequestRepository;
import ru.burdak.mainservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * The type Request service.
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor

public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<ParticipationRequestDto> getRequestsByUserPrivateController(HttpServletRequest httpServletRequest,
                                                                                  Long userId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        List<Request> request = requestRepository.findAllByRequester_Id(userId);
        return request.stream().map(RequestMapper::toDto).toList();
    }

    @Override
    public ParticipationRequestDto patchCancelRequestByIdPrivateController(HttpServletRequest httpRequest, Long userId,
                                                                           Long requestId) {

        Request request = requestRepository.findByRequester_IdAndId(userId, requestId).orElseThrow(
            () -> new NotFoundException(
                "Request with id=" + requestId + " and requester_id=)" + userId + " were not " + "found"));

        request.setStatus(RequestStatus.CANCELED);
        Request savedRequest = requestRepository.save(request);
        return RequestMapper.toDto(savedRequest);
    }

    @Override
    @Transactional
    public ParticipationRequestDto postRequestByUserPrivateController(Long userId, Long eventId) {

        if (requestRepository.existsByRequester_IdAndEvent_Id(userId, eventId)) {
            throw new ConflictException(
                "User with id= " + userId + " is already invited to the event with id=" + eventId);
        }
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User with id=" + userId + "was not found"));

        Request request = new Request();
        request.setStatus(RequestStatus.PENDING);
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);

        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException(
                "Initiator cannot to be joined to the event because it is Initiator that event");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConditionsNotMetException("The state of event should be Published");
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConditionsNotMetException("The limit of participants has been reached");
        }
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        return RequestMapper.toDto(requestRepository.save(request));
    }
}
