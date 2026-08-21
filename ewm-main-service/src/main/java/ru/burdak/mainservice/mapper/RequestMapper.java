package ru.burdak.mainservice.mapper;

import lombok.experimental.UtilityClass;
import ru.burdak.mainservice.dto.request.ParticipationRequestDto;
import ru.burdak.mainservice.model.Request;

@UtilityClass
public class RequestMapper {
    public ParticipationRequestDto toDto(Request request) {
        return new ParticipationRequestDto(request.getCreated(),
            request
                .getEvent()
                .getId(),
            request.getId(),
            request
                .getRequester()
                .getId(),
            request.getStatus());
    }

}
