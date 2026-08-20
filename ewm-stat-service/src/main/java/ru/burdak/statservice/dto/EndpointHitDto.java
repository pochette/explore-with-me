package ru.burdak.statservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record EndpointHitDto(
    Long id,

    @NotBlank
    String app,
    @NotBlank
    String uri,
    @NotBlank
    String ip,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp
) {
}
