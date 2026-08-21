package ru.burdak.mainservice.controller.admin_controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.burdak.mainservice.dto.event.EventFullDto;
import ru.burdak.mainservice.dto.event.UpdateEventAdminRequest;
import ru.burdak.mainservice.model.EventState;
import ru.burdak.mainservice.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.burdak.mainservice.util.FormatterDateTime.DATE_TIME_PATTERN;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEventsByAdmin(HttpServletRequest request,
                                                               @RequestParam(required = false, name = "users") List<Long> users,
                                                               @RequestParam(required = false, name = "states") List<EventState> states,
                                                               @RequestParam(required = false, name = "categories") List<Long> categories,
                                                               @RequestParam(required = false, name = "rangeStart")
                                                               @DateTimeFormat(pattern = DATE_TIME_PATTERN)
                                                               LocalDateTime rangeStart,
                                                               @RequestParam(required = false, name = "rangeEnd")
                                                               @DateTimeFormat(pattern = DATE_TIME_PATTERN)
                                                               LocalDateTime rangeEnd,
                                                               @RequestParam(required = false, name = "from", defaultValue = "0")
                                                               Integer from,
                                                               @RequestParam(required = false, name = "size", defaultValue = "10")
                                                               Integer size) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        log.info("AdminEventController.getEventsByAdmin called with parameters: users={}, states={}, categories={}, " +
                "rangeStart={}, rangeEnd={}, from={}, size={}", users, states, categories, rangeStart, rangeEnd, from,
            size);
        return new ResponseEntity<>(
            eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size, request),
            HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patchEventAndStatusAdmin(HttpServletRequest request,
                                                                 @PathVariable(name = "eventId") Long eventId,
                                                                 @RequestBody @Valid UpdateEventAdminRequest updateRequest) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        log.info("AdminEventController.patchEventAndStatusAdmin called with parameters: eventId={}, updateRequest={}",
            eventId, updateRequest);
        return ResponseEntity.ok(eventService.patchEventAndStatusAdmin(eventId, updateRequest));

    }
}
