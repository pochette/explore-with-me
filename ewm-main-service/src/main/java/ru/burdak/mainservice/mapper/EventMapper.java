package ru.burdak.mainservice.mapper;

import lombok.experimental.UtilityClass;
import ru.burdak.mainservice.dto.event.EventFullDto;
import ru.burdak.mainservice.dto.event.EventShortDto;
import ru.burdak.mainservice.dto.event.NewEventDto;
import ru.burdak.mainservice.dto.event.UpdateEventUserRequest;
import ru.burdak.mainservice.model.Event;
import ru.burdak.mainservice.model.EventState;

import java.time.LocalDateTime;
import java.util.Optional;

@UtilityClass
public class EventMapper {

    public Event toEntity(NewEventDto dto) {
        Event event = new Event();
        event.setCategory(null);
        event.setEventDate(dto.eventDate());
        event.setConfirmedRequests(0);
        event.setCreatedOn(LocalDateTime.now());
        event.setAnnotation(dto.annotation());
        event.setDescription(dto.description());
        event.setInitiator(null);
        event.setLocation(null);
        event.setPaid(dto.paid() != null && dto.paid());
        event.setParticipantLimit(dto.participantLimit() == null ? 0 : dto.participantLimit());
        event.setPublishedOn(null);
        event.setRequestModeration(dto.requestModeration() == null || dto.requestModeration());
        event.setState(EventState.PENDING);
        event.setTitle(dto.title());
        event.setViews(0L);
        return event;
    }

    public EventFullDto toFullDto(Event event) {
        return new EventFullDto(
            event.getId(),
            event.getAnnotation(),
            CategoryMapper.toDto(event.getCategory()),
            event.getConfirmedRequests(),
            event.getCreatedOn(),
            event.getDescription(),
            event.getEventDate(),
            UserMapper.toShortDto(event.getInitiator()),
            LocationMapper.toDto(event.getLocation()),
            event.getPaid(),
            event.getParticipantLimit(),
            event.getPublishedOn(),
            event.getRequestModeration(),
            event.getState(),
            event.getTitle(),
            event.getViews());
    }

    public EventShortDto toShortDto(Event event) {
        return new EventShortDto(
            event.getId(),
            event.getAnnotation(),
            CategoryMapper.toDto(event.getCategory()),
            event.getConfirmedRequests(),
            event.getEventDate(),
            UserMapper.toShortDto(event.getInitiator()),
            event.getPaid(),
            event.getTitle(), event.getViews());
    }

    public void updateFromUserRequest(Event event, UpdateEventUserRequest dto) {
        Optional.ofNullable(dto.annotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(dto.description()).ifPresent(event::setDescription);
        Optional.ofNullable(dto.title()).ifPresent(event::setTitle);
        Optional.ofNullable(dto.eventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(dto.paid()).ifPresent(event::setPaid);
        Optional.ofNullable(dto.participantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(dto.requestModeration()).ifPresent(event::setRequestModeration);

    }
}
