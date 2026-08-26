package ru.burdak.mainservice.controller.public_controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.burdak.mainservice.dto.location.AdminLocationDto;
import ru.burdak.mainservice.model.AdminLocation;
import ru.burdak.mainservice.service.AdminLocationService;

import java.util.List;

@RestController
@RequestMapping("/locations")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PublicLocationController {
    private final AdminLocationService service;

    @GetMapping("{locationId}/events")
    public List<AdminLocationDto> getLocationsListByIdPublic(HttpServletRequest request,
                                                            @PathVariable(name = "locationId") @PositiveOrZero
                                                         Long locationId) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return service.getLocationsListByIdPublic(locationId);

    }
}