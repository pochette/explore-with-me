package ru.burdak.mainservice.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.burdak.mainservice.dto.event.EventShortDto;

import java.util.Set;

public record CompilationDto(
    Set<EventShortDto> events,
    @NotNull @Positive Long id,
    @NotNull Boolean pinned,
    @NotBlank String title) {
}
