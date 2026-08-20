package ru.burdak.statservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.burdak.statservice.dto.EndpointHitDto;
import ru.burdak.statservice.service.StatService;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto postNewHit(
        @RequestBody @Valid EndpointHitDto dto
    ) {
        return statService.postNewHit(dto);
    }
}
