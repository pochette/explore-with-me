package ru.burdak.mainservice.controller.public_controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.burdak.mainservice.service.CompilationService;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {
    private final CompilationService service;



}
