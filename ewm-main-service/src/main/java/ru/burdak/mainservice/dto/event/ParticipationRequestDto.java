package ru.burdak.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.burdak.mainservice.model.ParticipationRequestStatus;

import java.time.LocalDateTime;

public record ParticipationRequestDto(
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created,

    Long event,

    Long id,

    Long requester,

    ParticipationRequestStatus status

) {
}
