package ru.burdak.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.burdak.mainservice.dto.location.LocationDto;
import ru.burdak.mainservice.model.StateAction;

import java.time.LocalDateTime;

public record UpdateEventAdminRequest(

    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation,

    Long category,

    @NotBlank
    @Size(min = 20, max = 7000)
    String description,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    LocalDateTime eventDate,

    LocationDto location,

    Boolean paid,

    Integer participantLimit,

    Boolean requestModeration,

    StateAction stateAction,

    @NotBlank
    @Size(min = 3, max = 120)
    String title
) {
}
