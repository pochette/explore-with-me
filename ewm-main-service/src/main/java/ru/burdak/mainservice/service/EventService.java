package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import ru.burdak.mainservice.dto.event.EventFullDto;
import ru.burdak.mainservice.dto.event.EventShortDto;
import ru.burdak.mainservice.dto.event.NewEventDto;
import ru.burdak.mainservice.dto.event.UpdateEventUserRequest;

import java.util.List;

public interface EventService {

    EventFullDto getEventByUserIdAndEventId(HttpServletRequest request, Long userId, Long eventId);

    List<EventShortDto> getEventsByUserId(HttpServletRequest request, Long userId, Integer from, Integer size);

    EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventFullDto postNewEvent(HttpServletRequest request, Long userId, NewEventDto newEventDto);
}
