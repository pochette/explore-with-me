package ru.burdak.mainservice.controller.private_controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.burdak.mainservice.dto.event.*;
import ru.burdak.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.burdak.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.burdak.mainservice.dto.request.ParticipationRequestDto;
import ru.burdak.mainservice.service.EventService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventByUserIdAndEventId(
        HttpServletRequest request,
        @PathVariable(name = "userId") @Positive Long userId,
        @PathVariable(name = "eventId", required = false) @Positive Long eventId) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ResponseEntity<>(
            eventService.getEventByUserIdAndEventId(userId, eventId),
            HttpStatus.OK
        );
    }

    @GetMapping()
    public ResponseEntity<Set<EventShortDto>> getEventsByUserId(
        HttpServletRequest request,
        @PathVariable(name = "userId") @Positive Long userId,
        @RequestParam(name = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
        @RequestParam(name = "size", required = false, defaultValue = "10") @Positive Integer size) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ResponseEntity<>(eventService.getEventsByUserId(userId, from, size),
            HttpStatus.OK
        );
    }

    @GetMapping("{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getParticipationRequestDto(
        HttpServletRequest request,
        @PathVariable(name = "userId") @Positive Long userId,
        @PathVariable(name = "eventId") @Positive Long eventId) {

        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ResponseEntity<>(
            eventService.getParticipationRequestDtoList(userId, eventId),
            HttpStatus.OK
        );
    }

    @PatchMapping("{eventId}")
    public ResponseEntity<EventFullDto> patchEvent(
        HttpServletRequest request,
        @PathVariable(name = "userId") @Positive Long userId,
        @PathVariable(name = "eventId") @Positive Long eventId,
        @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ResponseEntity<>(
            eventService.patchEvent(userId, eventId, updateEventUserRequest),
            HttpStatus.OK
        );
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> patchStatusOfRequestByUser(
        HttpServletRequest request,
        @PathVariable(name = "userId") @Positive Long userid,
        @PathVariable(name = "eventId") @Positive Long eventId,
        @RequestBody @Valid EventRequestStatusUpdateRequest requestDto) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ResponseEntity<>(
            eventService.patchStatusOfRequestByUser(userid, eventId, requestDto),
            HttpStatus.OK
        );

    }

    @PostMapping()
    public ResponseEntity<EventFullDto> postNewEvent(
        HttpServletRequest request,
        @PathVariable(name = "userId") @Positive Long userId,
        @RequestBody @Valid NewEventDto newEventDto) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ResponseEntity<>(
            eventService.postNewEvent(request, userId, newEventDto),
            HttpStatus.CREATED);
    }

}
