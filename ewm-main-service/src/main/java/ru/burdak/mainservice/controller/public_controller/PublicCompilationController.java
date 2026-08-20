package ru.burdak.mainservice.controller.public_controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.burdak.mainservice.dto.compilation.CompilationDto;
import ru.burdak.mainservice.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated

public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilationsPublic(HttpServletRequest httpRequest,
                                                                      @RequestParam(name = "pinned", required = false)
                                                                      Boolean pinned,
                                                                      @RequestParam(name = "from", defaultValue = "0")
                                                                      @PositiveOrZero Integer from,
                                                                      @RequestParam(name = "size",
                                                                          defaultValue = "10") @PositiveOrZero
                                                                      Integer size) {
        log.info("{} {} {}", httpRequest.getMethod(), httpRequest.getRequestURI(), httpRequest.getQueryString());

        return new ResponseEntity<>(compilationService.getCompilationsPublic(httpRequest, pinned, from, size),
            HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilationByIdPublic(HttpServletRequest httpRequest,
                                                                   @RequestParam(name = "compId") @Positive Long compId) {
        log.info("{} {} {}", httpRequest.getMethod(), httpRequest.getRequestURI(), httpRequest.getQueryString());
        return new ResponseEntity<>(compilationService.getCompilationByIdPublic(httpRequest, compId), HttpStatus.OK);
    }
}