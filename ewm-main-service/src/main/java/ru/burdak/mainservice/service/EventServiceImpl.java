package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.burdak.mainservice.dto.event.*;
import ru.burdak.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.burdak.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.burdak.mainservice.dto.request.ParticipationRequestDto;
import ru.burdak.mainservice.exception.ConditionsNotMetException;
import ru.burdak.mainservice.exception.NotFoundException;
import ru.burdak.mainservice.mapper.EventMapper;
import ru.burdak.mainservice.mapper.LocationMapper;
import ru.burdak.mainservice.mapper.RequestMapper;
import ru.burdak.mainservice.model.*;
import ru.burdak.mainservice.repository.*;
import ru.burdak.mainservice.util.EventSpecification;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
    public EventShortDto getEventByIdPublic(HttpServletRequest httpRequest, Long id) {
        Event event = eventRepository.findByIdAndStateEquals(id, EventState.PUBLISHED)
            .orElseThrow(() -> new NotFoundException("Event with id= " + id + " was not found"));
        log.info("Found event with id= {}", id);
        //TODO добавить сохранение статистики просмотров
        return EventMapper.toShortDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByUserIdAndEventId(HttpServletRequest request, Long userId, Long eventId) {
        Event event = eventRepository
            .findByInitiator_IdAndId(userId, eventId)
            .orElseThrow(() ->
                new NotFoundException(
                    "Event with id= " + eventId + " and initiator with id= " + userId + "was not " + "found"));
        log.info("Found event with id= {} and initiator with id= {}", eventId, userId);
        return EventMapper.toFullDto(event);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                               Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("eventDate").ascending());

        Specification<Event> spec =
            Specification.where(null);

        spec = spec.and(EventSpecification.hasInitiators(users))
            .and(EventSpecification.hasStates(states))
            .and(EventSpecification.hasCategories(categories))
            .and(EventSpecification.hasEventDateAfterOrEqual(rangeStart))
            .and(EventSpecification.hasEventDateBeforeOreEqual(rangeEnd));
        Set<Event> events = eventRepository.findAll(spec, pageable).stream().collect(Collectors.toSet());
        log.info("Found {} events with filters: users={}, states={}, categories={}, rangeStart={}, rangeEnd={}",
            events.size(), users, states, categories, rangeStart, rangeEnd);

        return events.stream()
            .map(EventMapper::toFullDto)
            .toList();
    }

    @Override
    public Collection<EventShortDto> getEventsByFilterPublic(String text, Set<Long> categoriesIds, Boolean paid,
                                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                             Boolean onlyAvailable, EventSortAvailable sortAvailable,
                                                             Integer from, Integer size) {
        Pageable pageable = PageRequest.of(
            from / size,
            size,
            getPublicEventSort(sortAvailable));

        Specification<Event> spec = Specification.where(EventSpecification.hasStates(List.of(EventState.PUBLISHED)))
            .and(EventSpecification.hasRangeDate(rangeStart, rangeEnd))
            .and(EventSpecification.hasText(text))
            .and(EventSpecification.hasCategories(categoriesIds))
            .and(EventSpecification.hasPaid(paid))
            .and(EventSpecification.hasOnlyAvailable(onlyAvailable));

        Set<Event> events = eventRepository.findAll(spec, pageable).stream().collect(Collectors.toSet());
        log.info(
            "Found {} events with filters: text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, " +
                "onlyAvailable={}, sortAvailable={}",
            events.size(), text, categoriesIds, paid, rangeStart, rangeEnd, onlyAvailable, sortAvailable);
        //TODO добавить сохранение статистики просмотров
        return events.stream()
            .map(EventMapper::toShortDto)
            .collect(Collectors.toSet());

    }

    @Override
    @Transactional(readOnly = true)
    public Set<EventShortDto> getEventsByUserId(HttpServletRequest request, Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort
            .by("id")
            .ascending());

        Set<Event> events = new HashSet<>(eventRepository.findAllByInitiator_Id(userId, pageable));
        log.info("Found {} events for user with id= {}", events.size(), userId);

        return events.stream()
            .map(EventMapper::toShortDto)
            .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getParticipationRequestDtoList(Long userId, Long eventId) {
        return requestRepository
            .findAllByEvent_Id(eventId)
            .stream()
            .map(RequestMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    public EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequest dto) {
        Event event = eventRepository
            .findByInitiator_IdAndId(userId, eventId)
            .orElseThrow(() ->
                new NotFoundException(
                    "Event with id= " + eventId + " and user with id=" + userId + " was not " + "found"));

        if (event.getState() != EventState.CANCELED && event.getState() != EventState.PENDING) {
            throw new ConditionsNotMetException("Only pending or canceled events can be changed");
        }

        if (dto.eventDate() != null && dto
            .eventDate()
            .isBefore(LocalDateTime
                .now()
                .plusHours(2))) {
            throw new ConditionsNotMetException("Event date must be at least two hours after now");
        }

        if (dto.category() != null) {
            Category category = categoryRepository
                .findById(dto.category())
                .orElseThrow(() ->
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
        log.info("Event with id= {} updated by user with id= {}", eventId, userId);
        log.info("Updated event: {}", event);
        return EventMapper.toFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto patchEventAndStatusAdmin(Long eventId, UpdateEventAdminRequest updateRequest) {
        Event event = eventRepository
            .findById(eventId)
            .orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));

        if (updateRequest.stateAction() == StateAction.REJECT_EVENT && event.getState() == EventState.PUBLISHED) {
            throw new ConditionsNotMetException("Cannot reject the event because it is Published");
        }
        if (updateRequest.stateAction() == StateAction.PUBLISH_EVENT && event.getState() != EventState.PENDING) {
            throw new ConditionsNotMetException("Cannot public the event because it has not Pending state");
        }

        updateIfPresent(updateRequest.eventDate(), event::setEventDate);

        if (updateRequest.stateAction() == StateAction.PUBLISH_EVENT && event
            .getEventDate()
            .isBefore(event
                .getCreatedOn()
                .plusHours(1))) {
            throw new ConditionsNotMetException("Start the event will be less then 1 hour");
        }

        updateIfPresent(updateRequest.annotation(), event::setAnnotation);
        updateIfPresent(updateRequest.category(), cat -> event.setCategory(categoryRepository
            .findById(cat)
            .orElseThrow(
                () -> new NotFoundException("Category with id= " + cat + " is not exists ")
            )));

        updateIfPresent(updateRequest.description(), event::setDescription);
        updateIfPresent(updateRequest.location(), loc ->
            event.setLocation(locationRepository.save(LocationMapper.toEntity(loc))));

        updateIfPresent(updateRequest.paid(), event::setPaid);
        updateIfPresent(updateRequest.participantLimit(), event::setParticipantLimit);

        if (updateRequest.stateAction() == StateAction.PUBLISH_EVENT) {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        }
        if (updateRequest.stateAction() == StateAction.REJECT_EVENT) {
            event.setState(EventState.CANCELED);
        }
        updateIfPresent(updateRequest.stateAction(),
            status -> event.setState(EventState.valueOf(status.name())));
        updateIfPresent(updateRequest.title(), event::setTitle);

        log.info("Event with id= {} updated by admin. Updated event: {}", eventId, event);

        return EventMapper.toFullDto(event);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult patchStatusOfRequestByUser(Long userid, Long eventId,
                                                                     EventRequestStatusUpdateRequest requestDto) {

        Event event = eventRepository
            .findByInitiator_IdAndId(userid, eventId)
            .orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " and user with id=" + userid + " was not found"));

        List<Request> requests = requestRepository.findAllByIdInAndEvent_Id(requestDto.requestIds(), eventId);

        if (requests.size() != requestDto
            .requestIds()
            .size()) {
            throw new NotFoundException("Request was not found");
        }
        if (requests
            .stream()
            .anyMatch(req -> req.getStatus() != RequestStatus.PENDING)) {
            throw new ConditionsNotMetException("Request must have status PENDING");
        }

        if (requestDto
            .status()
            .equals(RequestStatus.REJECTED)) {
            return rejectRequests(requests);
        }
        log.info("Confirming requests for event with id= {} by user with id= {}", eventId, userid);
        log.info("Requests to confirm: {}", requests);
        return confirmedRequests(eventId, event, requests);
    }

    private EventRequestStatusUpdateResult rejectRequests(List<Request> requests) {
        Set<ParticipationRequestDto> confirmedRequests = new HashSet<>();
        Set<ParticipationRequestDto> rejectedRequests = new HashSet<>();

        for (Request request : requests) {
            request.setStatus(RequestStatus.REJECTED);
            rejectedRequests.add(RequestMapper.toDto(request));
        }
        log.info("Rejected requests: {}", rejectedRequests);
        log.info("Confirmed requests: {}", confirmedRequests);
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
        log.info("Rejected requests: {}", rejectedRequests);
        log.info("Confirmed requests: {}", confirmedRequests);
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    @Transactional
    @Override
    public EventFullDto postNewEvent(HttpServletRequest request, Long userId, NewEventDto newEventDto) {
        User user = userRepository
            .findById(userId)
            .orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " was not found"));
        Category category = categoryRepository
            .findById(newEventDto.category())
            .orElseThrow(() ->
                new NotFoundException("Category with id=" + newEventDto.category() + " was not found"));
        Location location = locationRepository.save(LocationMapper.toEntity(newEventDto.location()));
        Event event = EventMapper.toEntity(newEventDto);
        event.setLocation(location);
        event.setInitiator(user);
        event.setCategory(category);
        log.info("Creating new event: {}", event);
        return EventMapper.toFullDto(eventRepository.save(event));
    }

    private <T> void updateIfPresent(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private Sort getPublicEventSort(EventSortAvailable sortAvailable) {
        if (sortAvailable == EventSortAvailable.VIEWS) {
            return Sort.by("views").descending();
        } else if (sortAvailable == EventSortAvailable.EVENT_DATE) {
            return Sort.by("eventDate").ascending();
        }
        return Sort.by("id").ascending();
    }
}
