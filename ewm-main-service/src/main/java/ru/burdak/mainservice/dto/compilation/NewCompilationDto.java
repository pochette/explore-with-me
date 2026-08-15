package ru.burdak.mainservice.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record NewCompilationDto (

    @NotNull
    Set<Long> events,

    Boolean pinned,

    @NotBlank
    @Size(min = 1, max = 50)
    String title
) {
}
