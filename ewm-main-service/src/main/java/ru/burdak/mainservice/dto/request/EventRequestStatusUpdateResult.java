package ru.burdak.mainservice.dto.request;

import java.util.Set;

public record EventRequestStatusUpdateResult(
    Set<ParticipationRequestDto> confirmedRequests,
    Set<ParticipationRequestDto> rejectedRequests) {
}
