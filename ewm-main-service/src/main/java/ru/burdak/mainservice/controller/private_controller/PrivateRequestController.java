package ru.burdak.mainservice.controller.private_controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
        HttpServletRequest request,
        @PathVariable(name = "userId") Long userId) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ResponseEntity<>(requestService.getRequestsByUserPrivateController(request, userId),
            HttpStatus.OK);
    }


}
