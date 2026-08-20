package ru.burdak.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import ru.burdak.mainservice.dto.location.LocationDto;
import ru.burdak.mainservice.model.UserStateAction;

import java.time.LocalDateTime;

public record UpdateEventUserRequest(


    @Size(min = 20, max = 2000)
    String annotation,

    @Positive
    Long category,


    @Size(min = 20, max = 7000)
    String description,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    LocalDateTime eventDate,

    LocationDto location,

    Boolean paid,

    @PositiveOrZero
    Integer participantLimit,

    Boolean requestModeration,

    UserStateAction stateAction,


    @Size(min = 3, max = 120)
    String title

) {
}
