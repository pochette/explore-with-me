package ru.burdak.statservice.dto;

import jakarta.validation.constraints.NotBlank;

public record ViewStatsDto(
    @NotBlank
    String app,
    @NotBlank
    String uri,

    Long hits
) {}
