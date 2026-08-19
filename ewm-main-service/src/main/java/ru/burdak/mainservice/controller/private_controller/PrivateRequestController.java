package ru.burdak.mainservice.controller.private_controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.burdak.mainservice.dto.request.ParticipationRequestDto;
import ru.burdak.mainservice.service.RequestService;

import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PrivateRequestController {
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<Collection<ParticipationRequestDto>> getRequestsByUserPrivateController(
        HttpServletRequest request, @PathVariable(name = "userId") Long userId) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ResponseEntity<>(requestService.getRequestsByUserPrivateController(request, userId), HttpStatus.OK);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> patchCancelRequestByIdPrivateController(HttpServletRequest request,
                                                                                           @PathVariable(name =
                                                                                               "userId")
                                                                                           @Positive Long userId,
                                                                                           @PathVariable(name =
                                                                                               "requestId")
                                                                                           @Positive Long requestId) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ResponseEntity<>(requestService.patchCancelRequestByIdPrivateController(request, userId, requestId),
            HttpStatus.OK);

    }

    @PostMapping()
    public ResponseEntity<ParticipationRequestDto> postRequestByUserPrivateController(HttpServletRequest request,
                                                                                      @PathVariable(name = "userId")
                                                                                      @Positive Long userId,
                                                                                      @RequestParam(name = "eventId")
                                                                                      @Positive Long evenId) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ResponseEntity<>(requestService.postRequestByUserPrivateController(userId, evenId), HttpStatus.OK);
    }
}
