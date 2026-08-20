package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.burdak.mainservice.dto.event.*;
import ru.burdak.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.burdak.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.burdak.mainservice.dto.request.ParticipationRequestDto;
import ru.burdak.mainservice.model.EventSortAvailable;
import ru.burdak.mainservice.model.EventState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface EventService {

    EventShortDto getEventByIdPublic(HttpServletRequest httpRequest, Long id);

    EventFullDto getEventByUserIdAndEventId(HttpServletRequest request, Long userId, Long eventId);

    List<EventFullDto> getEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    Collection<EventShortDto> getEventsByFilterPublic(String text, Set<Long> categoriesIds, Boolean paid,
                                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                      Boolean onlyAvailable, EventSortAvailable sortAvailable,
                                                      Integer from, Integer size);

    Set<EventShortDto> getEventsByUserId(HttpServletRequest request, Long userId, Integer from, Integer size);

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     *
     * @param userId
     * @param eventId
     * @return ParticipationRequestDto
     */
    List<ParticipationRequestDto> getParticipationRequestDtoList(Long userId, Long eventId);

    EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventFullDto patchEventAndStatusAdmin(Long eventId, UpdateEventAdminRequest updateRequest);

    EventRequestStatusUpdateResult patchStatusOfRequestByUser(Long userid, Long eventId,
                                                              EventRequestStatusUpdateRequest requestDto);

    EventFullDto postNewEvent(HttpServletRequest request, Long userId, NewEventDto newEventDto);
}
