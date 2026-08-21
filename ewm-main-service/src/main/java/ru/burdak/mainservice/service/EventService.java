package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.burdak.mainservice.dto.event.*;
import ru.burdak.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.burdak.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.burdak.mainservice.dto.request.ParticipationRequestDto;
import ru.burdak.mainservice.model.EventSortAvailable;
import ru.burdak.mainservice.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * The interface Event service.
 */
public interface EventService {

    /**
     * Gets event by id public.
     *
     * @param httpRequest the http request
     * @param id          the id
     * @return the event by id public
     */
    EventFullDto getEventByIdPublic(HttpServletRequest httpRequest, Long id);

    /**
     * Gets event by user id and event id.
     *
     * @param request the request
     * @param userId  the user id
     * @param eventId the event id
     * @return the event by user id and event id
     */
    EventFullDto getEventByUserIdAndEventId(HttpServletRequest request, Long userId, Long eventId);

    /**
     * Gets events by admin.
     *
     * @param users      the users
     * @param states     the states
     * @param categories the categories
     * @param rangeStart the range start
     * @param rangeEnd   the range end
     * @param from       the from
     * @param size       the size
     * @return the events by admin
     */
    List<EventFullDto> getEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    /**
     * Gets events by filter public.
     *
     * @param text          the text
     * @param categoriesIds the categories ids
     * @param paid          the paid
     * @param rangeStart    the range start
     * @param rangeEnd      the range end
     * @param onlyAvailable the only available
     * @param sortAvailable the sort available
     * @param from          the from
     * @param size          the size
     * @return the events by filter public
     */
    List<EventShortDto> getEventsByFilterPublic(String text, Set<Long> categoriesIds, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                Boolean onlyAvailable, EventSortAvailable sortAvailable,
                                                Integer from, Integer size);

    /**
     * Gets events by user id.
     *
     * @param request the request
     * @param userId  the user id
     * @param from    the from
     * @param size    the size
     * @return the events by user id
     */
    Set<EventShortDto> getEventsByUserId(HttpServletRequest request, Long userId, Integer from, Integer size);

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     *
     * @param userId  the user id
     * @param eventId the event id
     * @return ParticipationRequestDto
     */
    List<ParticipationRequestDto> getParticipationRequestDtoList(Long userId, Long eventId);

    /**
     * Patch event event full dto.
     *
     * @param userId                 the user id
     * @param eventId                the event id
     * @param updateEventUserRequest the update event user request
     * @return the event full dto
     */
    EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    /**
     * Patch event and status admin event full dto.
     *
     * @param eventId       the event id
     * @param updateRequest the update request
     * @return the event full dto
     */
    EventFullDto patchEventAndStatusAdmin(Long eventId, UpdateEventAdminRequest updateRequest);

    /**
     * Patch status of request by user event request status update result.
     *
     * @param userid     the userid
     * @param eventId    the event id
     * @param requestDto the request dto
     * @return the event request status update result
     */
    EventRequestStatusUpdateResult patchStatusOfRequestByUser(Long userid, Long eventId,
                                                              EventRequestStatusUpdateRequest requestDto);

    /**
     * Post new event event full dto.
     *
     * @param request     the request
     * @param userId      the user id
     * @param newEventDto the new event dto
     * @return the event full dto
     */
    EventFullDto postNewEvent(HttpServletRequest request, Long userId, NewEventDto newEventDto);
}
