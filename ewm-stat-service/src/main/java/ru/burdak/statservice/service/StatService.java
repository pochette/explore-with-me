package ru.burdak.statservice.service;


import ru.burdak.ewmstatsclient.dto.EndpointHitDto;
import ru.burdak.ewmstatsclient.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    EndpointHitDto postNewHit(EndpointHitDto dto);
}
