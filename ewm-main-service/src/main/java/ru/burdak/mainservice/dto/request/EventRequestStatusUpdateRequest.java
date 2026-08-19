package ru.burdak.mainservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.burdak.mainservice.model.RequestStatus;

import java.util.Set;

public record EventRequestStatusUpdateRequest (
    @NotNull
    @Positive
    Set<Long> requestIds,

    @NotNull
    RequestStatus status
) {
}
