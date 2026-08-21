package ru.burdak.mainservice.controller.admin_controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.burdak.mainservice.dto.location.AdminLocationDto;
import ru.burdak.mainservice.dto.location.NewAdminLocationDto;
import ru.burdak.mainservice.service.AdminLocationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/locations")
@Slf4j
@Validated
public class AdminLocationController {
    private AdminLocationService adminLocationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminLocationDto addNewAdminLocation(HttpServletRequest request,
                                                @RequestBody @Valid NewAdminLocationDto dto) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return adminLocationService.addNewAdminLocation(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AdminLocationDto> getLocationsListByFilter(
        HttpServletRequest request,
        @RequestParam(required = false, name = "from", defaultValue =
            "0") @PositiveOrZero Integer from,
        @RequestParam(required = false, name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return adminLocationService.getLocationsListByFilter(from, size);
    }
}
