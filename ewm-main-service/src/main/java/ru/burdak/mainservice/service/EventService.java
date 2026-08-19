package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import ru.burdak.mainservice.dto.event.*;
import ru.burdak.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.burdak.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.burdak.mainservice.dto.request.ParticipationRequestDto;
import ru.burdak.mainservice.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto getEventByUserIdAndEventId(HttpServletRequest request, Long userId, Long eventId);

    List<EventFullDto> getEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    List<EventShortDto> getEventsByUserId(HttpServletRequest request, Long userId, Integer from, Integer size);

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     * @param userId
     * @param eventId
     * @return ParticipationRequestDto
     */
    List<ParticipationRequestDto> getParticipationRequestDtoList(Long userId, Long eventId);

    EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventFullDto patchEventAndStatusAdmin(Long eventId, UpdateEventAdminRequest updateRequest);

    EventRequestStatusUpdateResult patchStatusOfRequestByUser(Long userid, Long eventId, EventRequestStatusUpdateRequest requestDto);

    EventFullDto postNewEvent(HttpServletRequest request, Long userId, NewEventDto newEventDto);
}
