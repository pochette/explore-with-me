package ru.burdak.mainservice.controller.public_controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.burdak.ewmstatsclient.client.StatsClient;
import ru.burdak.mainservice.dto.event.EventFullDto;
import ru.burdak.mainservice.dto.event.EventShortDto;
import ru.burdak.mainservice.model.EventSortAvailable;
import ru.burdak.mainservice.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static ru.burdak.mainservice.util.FormatterDateTime.DATE_TIME_PATTERN;

/**
 * The type Public event controller.
 */
@RestController
@RequestMapping("/events")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PublicEventController {
    private final EventService eventService;
    private final StatsClient statsClient;

    /**
     * ё
     * Gets event by id public controller.
     *
     * @param httpRequest the http request
     * @param id          the id
     * @return the event by id public controller
     */

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEventByIdPublicController(
        HttpServletRequest httpRequest,
        @PathVariable(name = "id") @Positive Long id
    ) {
        log.info("{} {}?{}", httpRequest.getMethod(), httpRequest.getRequestURI(), httpRequest.getQueryString());
        EventFullDto event = eventService.getEventByIdPublic(httpRequest, id);
        return ResponseEntity.ok(event);
    }

    /**
     * Gets events by filter public controller.
     *
     * @param request       the http request
     * @param text          the text
     * @param categoriesIds the categories ids
     * @param paid          the paid
     * @param rangeStart    the range start
     * @param rangeEnd      the range end
     * @param onlyAvailable the only available
     * @param sortAvailable the sort available
     * @param from          the from параметр для постраничного вывода, начиная с какого элемента (0 - означает вывод
     *                      с первого элемента)
     * @param size          the size параметр для постраничного вывода, количество элементов для отображения
     * @return the events by filter public controller
     */
//todo Настроить сохранение статистики
    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsByFilterPublicController(
        HttpServletRequest request,
        @RequestParam(required = false, name = "text") String text,
        @RequestParam(required = false, name = "categories") Set<@Positive Long> categoriesIds,
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
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        log.info(
            "Parameters: text={}, categoriesIds={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, " +
                "sortAvailable={}, from={}, size={}",
            text, categoriesIds, paid, rangeStart, rangeEnd, onlyAvailable, sortAvailable, from, size);
        List<EventShortDto> events = eventService.getEventsByFilterPublic(
            text, categoriesIds, paid, rangeStart, rangeEnd, onlyAvailable, sortAvailable, from, size, request);
        return ResponseEntity.ok(events);

    }
}
