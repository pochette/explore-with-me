package ru.burdak.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import ru.burdak.mainservice.dto.location.LocationDto;
import ru.burdak.mainservice.validation.EventDateAfterHours;

import java.time.LocalDateTime;

/**
 * DTO for {@link ru.burdak.mainservice.model.Event}
 */
public record NewEventDto(
    @Size(min = 20, max = 2000) @NotBlank
    String annotation,

    @NotNull
    @Positive
    Long category,

    @Size(min = 20, max = 7000) @NotBlank
    String description,

    @NotNull
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EventDateAfterHours(hours = 2)
    LocalDateTime eventDate,

    @NotNull
    @Valid
    LocationDto location,

    Boolean paid,

    @PositiveOrZero
    Integer participantLimit,

    Boolean requestModeration,

    @Size(min = 3, max = 120) @NotBlank
    String title
) {
}