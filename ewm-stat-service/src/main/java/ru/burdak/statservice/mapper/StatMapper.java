package ru.burdak.statservice.mapper;

import lombok.experimental.UtilityClass;
import ru.burdak.statservice.dto.EndpointHitDto;
import ru.burdak.statservice.model.App;
import ru.burdak.statservice.model.Hit;

@UtilityClass
public class StatMapper {
    public EndpointHitDto toEndpointHitDto(Hit hit) {
        return new EndpointHitDto(
            hit.getId(),
            hit.getApp().getName(),
            hit.getUri(),
            hit.getIp(),
            hit.getTimestamp());
    }

    public Hit toHitEntity(EndpointHitDto dto, App app) {
        Hit hit = new Hit();
        hit.setApp(app);
        hit.setUri(dto.uri());
        hit.setIp(dto.ip());
        hit.setTimestamp(dto.timestamp());
        return hit;
    }

}
