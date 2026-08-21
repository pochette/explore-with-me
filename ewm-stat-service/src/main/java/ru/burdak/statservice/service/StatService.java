package ru.burdak.statservice.service;

import ru.burdak.statservice.dto.EndpointHitDto;

public interface StatService {
    EndpointHitDto postNewHit(EndpointHitDto dto);
}
