package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import ru.burdak.mainservice.dto.request.ParticipationRequestDto;

import java.util.Collection;

public interface RequestService {

    Collection<ParticipationRequestDto> getRequestsByUserPrivateController(HttpServletRequest httpServletRequest, Long userId);

    ParticipationRequestDto patchCancelRequestByIdPrivateController(HttpServletRequest request, Long userId, Long requestId);

    ParticipationRequestDto postRequestByUserPrivateController(Long userId, Long evenId);
}
