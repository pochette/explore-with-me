package ru.burdak.mainservice.controller.admin_controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.burdak.mainservice.dto.compilation.CompilationDto;
import ru.burdak.mainservice.dto.compilation.NewCompilationDto;
import ru.burdak.mainservice.dto.compilation.UpdateCompilationRequest;
import ru.burdak.mainservice.service.CompilationService;

@RestController
@RequestMapping("/admin/compilations")
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {
    private final CompilationService compilationService;

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilationByIdFromAdminController(
        HttpServletRequest httpRequest,
        @PathVariable(name = "compId") @Positive
        Long compId) {
        log.info("{} {}?{}", httpRequest.getMethod(), httpRequest.getRequestURI(), httpRequest.getQueryString());
        compilationService.deleteCompilationByIdFromAdminController(httpRequest, compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> patchCompilationByIdFromAdminController(HttpServletRequest httpRequest,
                                                                                  @PathVariable(name = "compId")
                                                                                  @Positive Long compId,
                                                                                  @RequestBody @Valid
                                                                                  UpdateCompilationRequest request) {
        log.info("{} {}?{}", httpRequest.getMethod(), httpRequest.getRequestURI(), httpRequest.getQueryString());
        return new ResponseEntity<>(compilationService.patchCompilationByIdFromAdminController(compId, request),
            HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<CompilationDto> postNewCompilationFromAdminController(HttpServletRequest httpRequest,
                                                                                @RequestBody @Valid
                                                                                NewCompilationDto dto) {
        log.info("{} {}?{}", httpRequest.getMethod(), httpRequest.getRequestURI(), httpRequest.getQueryString());
        return new ResponseEntity<>(compilationService.postNewCompilationFromAdminController(httpRequest, dto),
            HttpStatus.CREATED);
    }
}
