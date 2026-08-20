package ru.burdak.mainservice.dto.request;

import jakarta.validation.constraints.NotNull;
import ru.burdak.mainservice.model.RequestStatus;

import java.util.Set;

public record EventRequestStatusUpdateRequest(
    @NotNull
    Set<Long> requestIds,

    @NotNull
    RequestStatus status
) {
}
