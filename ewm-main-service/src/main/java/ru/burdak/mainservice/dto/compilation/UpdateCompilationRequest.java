package ru.burdak.mainservice.dto.compilation;

import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateCompilationRequest(Set<Long> events,

                                       Boolean pinned,

                                       @Size(max = 50, min = 1) String title) {
}
