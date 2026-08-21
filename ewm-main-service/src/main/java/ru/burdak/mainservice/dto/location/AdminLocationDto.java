package ru.burdak.mainservice.dto.location;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminLocationDto(
    @Nullable
    Long id,

    @NotBlank
    String name,

    @NotNull
    Float lat,

    @NotNull
    Float lon,

    @NotNull
    Double radius
) {
}
