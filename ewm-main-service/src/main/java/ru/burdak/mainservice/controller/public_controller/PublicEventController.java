package ru.burdak.mainservice.controller.public_controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.burdak.mainservice.dto.event.EventShortDto;
import ru.burdak.mainservice.model.EventSortAvailable;
import ru.burdak.mainservice.service.EventService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import static ru.burdak.mainservice.util.FormatterDateTime.DATE_TIME_PATTERN;

@RestController
@RequestMapping("/events")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Collection<EventShortDto>> getEventsByFilterPublicController(
        HttpServletRequest httpRequest,
        @RequestParam(required = false, name = "text") String text,
        @RequestParam(required = false, name = "categories") Set<Long> categoriesIds,
        @RequestParam(required = false, name = "paid") Boolean paid,
        @RequestParam(required = false, name = "rangeStart")
        @DateTimeFormat(pattern = DATE_TIME_PATTERN)
        LocalDateTime rangeStart,
        @RequestParam(required = false, name = "rangeEnd")
        @DateTimeFormat(pattern = DATE_TIME_PATTERN)
        LocalDateTime rangeEnd,
        @RequestParam(required = false, name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
        @RequestParam(required = false, name = "sort") EventSortAvailable sortAvailable,
        @RequestParam(required = false, name = "from", defaultValue = "0") Integer from,
        @RequestParam(required = false, name = "size", defaultValue = "10") Integer size
    ) {
        log.info("{} {}?{}", httpRequest.getMethod(), httpRequest.getRequestURI(), httpRequest.getQueryString());
        log.info("Parameters: text={}, categoriesIds={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sortAvailable={}, from={}, size={}",
            text, categoriesIds, paid, rangeStart, rangeEnd, onlyAvailable, sortAvailable, from, size);
        Collection<EventShortDto> events = eventService.getEventsByFilterPublic(
            text, categoriesIds, paid, rangeStart, rangeEnd, onlyAvailable, sortAvailable, from, size);
        return ResponseEntity.ok(events);

    }

    @GetMapping("/{id}")
    public ResponseEntity<EventShortDto> getEventByIdPublicController(
        HttpServletRequest httpRequest,
        @RequestParam(name = "id") @Positive Long id
    ) {
        log.info("{} {}?{}", httpRequest.getMethod(), httpRequest.getRequestURI(), httpRequest.getQueryString());
        EventShortDto event = eventService.getEventByIdPublic(httpRequest, id);
        return ResponseEntity.ok(event);
    }
}
