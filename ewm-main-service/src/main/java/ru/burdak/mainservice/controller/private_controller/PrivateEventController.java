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
import ru.burdak.mainservice.dto.event.EventFullDto;
import ru.burdak.mainservice.dto.event.EventShortDto;
import ru.burdak.mainservice.dto.event.NewEventDto;
import ru.burdak.mainservice.dto.event.UpdateEventUserRequest;
import ru.burdak.mainservice.service.EventService;
import ru.burdak.mainservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PrivateEventController {
    private final UserService userService;
    private final EventService eventService;

    @PatchMapping("/users/{userId}/events/{eventId}")
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

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getEventByUserIdAndEventId(
        HttpServletRequest request,
        @PathVariable(name = "userId") @Positive Long userId,
        @PathVariable(name = "eventId", required = false) @Positive Long eventId) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ResponseEntity<> (
            eventService.getEventByUserIdAndEventId(request, userId, eventId),
            HttpStatus.OK

            );
    }

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventShortDto>> getEventsByUserId(
        HttpServletRequest request,
        @PathVariable(name = "userId") @Positive Long userId,
        @RequestParam(name = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
        @RequestParam(name = "size", required = false, defaultValue = "10") @Positive Integer size) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ResponseEntity<>(eventService.getEventsByUserId(request, userId, from, size),
            HttpStatus.OK
        );
    }

        @PostMapping("/{userId}/events")
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
