package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.burdak.mainservice.dto.event.EventFullDto;
import ru.burdak.mainservice.dto.event.EventShortDto;
import ru.burdak.mainservice.dto.event.NewEventDto;
import ru.burdak.mainservice.dto.event.UpdateEventUserRequest;
import ru.burdak.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.burdak.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.burdak.mainservice.dto.request.ParticipationRequestDto;
import ru.burdak.mainservice.exception.BadRequestException;
import ru.burdak.mainservice.exception.ConditionsNotMetException;
import ru.burdak.mainservice.exception.NotFoundException;
import ru.burdak.mainservice.mapper.EventMapper;
import ru.burdak.mainservice.mapper.LocationMapper;
import ru.burdak.mainservice.mapper.RequestMapper;
import ru.burdak.mainservice.model.*;
import ru.burdak.mainservice.repository.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByUserIdAndEventId(HttpServletRequest request, Long userId, Long eventId) {
        Event event = eventRepository.findByInitiator_IdAndId(userId, eventId).orElseThrow(() ->
                new NotFoundException("Event with id= " + eventId + " and initiator with id= " + userId + "was not " + "found"));
        return EventMapper.toFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsByUserId(HttpServletRequest request, Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());

        return eventRepository.findAllByInitiator_Id(userId, pageable).stream().map(EventMapper::toShortDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getParticipationRequestDtoList(Long userId, Long eventId) {
        return requestRepository.findAllByEvent_Id(eventId).stream().map(RequestMapper::toDto).toList();
    }

    @Override
    @Transactional
    public EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequest dto) {
        Event event = eventRepository.findByInitiator_IdAndId(userId, eventId).orElseThrow(() ->
                new NotFoundException("Event with id= " + eventId + " and user with id=" + userId + " was not " + "found"));

        if (event.getState() != EventState.CANCELED && event.getState() != EventState.PENDING) {
            throw new ConditionsNotMetException("Only pending or canceled events can be changed");
        }

        if (dto.eventDate() != null && dto.eventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConditionsNotMetException("Event date must be at least two hours after now");
        }

        if (dto.category() != null) {
            Category category = categoryRepository.findById(dto.category()).orElseThrow(() ->
                    new NotFoundException("Category with id=" + dto.category() + " was not found"));
            event.setCategory(category);
        }

        if (dto.location() != null) {
            Location location = locationRepository.save(LocationMapper.toEntity(dto.location()));
            event.setLocation(location);
        }

        if (dto.stateAction() != null) {
            switch (dto.stateAction()) {
                case SEND_TO_REVIEW -> event.setState(EventState.PENDING);
                case CANCEL_REVIEW -> event.setState(EventState.CANCELED);
            }
        }

        EventMapper.updateFromUserRequest(event, dto);
        return EventMapper.toFullDto(event);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult patchStatusOfRequestByUser(Long userid, Long eventId,
                                                                     EventRequestStatusUpdateRequest requestDto) {

        Event event = eventRepository.findByInitiator_IdAndId(userid, eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " and user with id=" + userid + " was not found"));

        List<Request> requests = requestRepository.findAllByIdInAndEvent_Id(requestDto.requestIds(), eventId);

        if (requests.size() != requestDto.requestIds().size()) {
            throw new NotFoundException("Request was not found");
        }
        if (requests.stream().anyMatch(req -> req.getStatus() != RequestStatus.PENDING)) {
            throw new ConditionsNotMetException("Request must have status PENDING");
        }

        if (requestDto.status().equals(RequestStatus.REJECTED)) {
            return rejectRequests(requests);
        }

        //todo: прописать данную проверку при POST users/{userId}/requests в privateRequestController if (event
        // .getParticipantLimit() == 0 || !event.getRequestModeration()) {}
        //

        return confirmedRequests(eventId, event, requests);
    }

    private EventRequestStatusUpdateResult rejectRequests(List<Request> requests) {
        Set<ParticipationRequestDto> confirmedRequests = new HashSet<>();
        Set<ParticipationRequestDto> rejectedRequests = new HashSet<>();

        for (Request request : requests) {
            request.setStatus(RequestStatus.REJECTED);
            rejectedRequests.add(RequestMapper.toDto(request));
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private EventRequestStatusUpdateResult confirmedRequests(Long eventId, Event event, List<Request> requests) {
        Set<ParticipationRequestDto> confirmedRequests = new HashSet<>();
        Set<ParticipationRequestDto> rejectedRequests = new HashSet<>();

        Integer participantLimit = event.getParticipantLimit();
        Integer confirmedCount = event.getConfirmedRequests();
        if (participantLimit != 0 && confirmedCount >= participantLimit) {
            throw new ConditionsNotMetException("The participant limit has been reached");
        }
        for (Request request : requests) {
            if (participantLimit != 0 && confirmedCount >= participantLimit) {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(RequestMapper.toDto(request));
            } else {
                request.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(RequestMapper.toDto(request));
                confirmedCount++;
            }
        }
        event.setConfirmedRequests(confirmedCount);

        if (participantLimit != 0 && confirmedCount >= participantLimit) {
            List<Request> pendingRequests = requestRepository.findAllByEvent_IdAndStatus(
                    eventId, RequestStatus.PENDING);

            for (Request request : pendingRequests) {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(RequestMapper.toDto(request));
            }
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    @Transactional
    @Override
    public EventFullDto postNewEvent(HttpServletRequest request, Long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " was not found"));
        Category category = categoryRepository.findById(newEventDto.category()).orElseThrow(() ->
                new NotFoundException("Category with id=" + newEventDto.category() + " was not found"));
        Location location = locationRepository.save(LocationMapper.toEntity(newEventDto.location()));
        Event event = EventMapper.toEntity(newEventDto);
        event.setLocation(location);
        event.setInitiator(user);
        event.setCategory(category);

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    private <T> void updateIfPresent(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
