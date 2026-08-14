package ru.burdak.mainservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.burdak.mainservice.model.Event;

import java.time.LocalDateTime;

/**
 * DTO for {@link Event}
 */
public record EventShortDto(

    @NotNull Long id,

    @NotBlank
    String annotation,

    @NotNull CategoryDto category,

    Integer confirmedRequests,

    @Future @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate,

    @NotNull UserShortDto initiator,

    @NotNull Boolean paid,

    @NotBlank String title,

    Long views) {
}