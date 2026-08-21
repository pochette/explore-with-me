package ru.burdak.ewmstatsclient.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

import static ru.burdak.ewmstatsclient.client.StatsClient.DATE_TIME_PATTERN;

/**
 * The type Endpoint hit dto.
 */
public record EndpointHitDto(
    Long id,

    @NotBlank
    String app,
    @NotBlank
    String uri,
    @NotBlank
    String ip,
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    LocalDateTime timestamp
) {
}
