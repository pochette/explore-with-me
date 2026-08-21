package ru.burdak.mainservice.controller.admin_controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.burdak.mainservice.service.AdminLocationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/locations")
@Slf4j
@Validated
public class AdminLocationController {
    private AdminLocationService adminLocationService;

}
