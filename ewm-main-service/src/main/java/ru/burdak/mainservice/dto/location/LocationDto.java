package ru.burdak.mainservice.dto.location;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link ru.burdak.mainservice.model.Location}
 */
public record LocationDto(@NotNull Float lat, @NotNull Float lon) implements Serializable {
}