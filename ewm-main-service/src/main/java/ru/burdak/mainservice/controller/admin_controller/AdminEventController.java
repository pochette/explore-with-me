package ru.burdak.mainservice.controller.admin_controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@RestController
@RequestMapping("/admin/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEventsByAdmin(HttpServletRequest request,
                                               @RequestParam(name = "users") List<Long> users,
                                               @RequestParam(name = "states") List<EventState> states,
                                               @RequestParam(name = "categories") List<Long> categories,
                                               @RequestParam(name = "rangeStart") LocalDateTime rangeStart,
                                               @RequestParam(name = "rangeEnd") LocalDateTime rangeEnd,
                                               @RequestParam(name = "from", defaultValue = "0") Integer from,
                                               @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        log.info(
            "AdminEventController.getEventsByAdmin called with parameters: users={}, states={}, categories={}, " +
                "rangeStart={}, rangeEnd={}, from={}, size={}",
            users, states, categories, rangeStart, rangeEnd, from, size);
        return new ResponseEntity<>(eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd,
            from, size),
            HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patchEventAndStatusAdmin(HttpServletRequest request,
                                                                 @PathVariable(name = "eventId") Long eventId,
                                                                 @RequestBody UpdateEventAdminRequest updateRequest) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        log.info(
            "AdminEventController.patchEventAndStatusAdmin called with parameters: eventId={}, updateRequest={}",
            eventId, updateRequest);
        return new ResponseEntity<>(eventService.patchEventAndStatusAdmin(eventId, updateRequest),

            HttpStatus.OK);

    }
}
