package ru.burdak.mainservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import ru.burdak.mainservice.model.EventState;

import java.time.LocalDateTime;

/**
 * DTO for {@link ru.burdak.mainservice.model.Event}
 */
public record EventFullDto(

    Long id,

    @NotBlank
    String annotation,

    @NotNull
    CategoryDto category,

    Integer confirmedRequests,

    @NotNull @PastOrPresent @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn,

    @NotBlank
    String description,

    @NotNull @Future @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate,

    @NotNull
    UserShortDto initiator,

    @NotNull
    LocationDto location,

    @NotNull
    Boolean paid,

    Integer participantLimit,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn,

    Boolean requestModeration,

    EventState state,

    @NotBlank
    String title,

    Long views) {
}