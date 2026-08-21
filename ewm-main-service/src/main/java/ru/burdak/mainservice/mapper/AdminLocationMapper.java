package ru.burdak.mainservice.mapper;

import lombok.experimental.UtilityClass;
import ru.burdak.mainservice.dto.location.AdminLocationDto;
import ru.burdak.mainservice.dto.location.NewAdminLocationDto;
import ru.burdak.mainservice.dto.location.UpdateAdminLocationDto;
import ru.burdak.mainservice.model.AdminLocation;

@UtilityClass
public class AdminLocationMapper {
    public AdminLocationDto toDto(AdminLocation location) {
        return new AdminLocationDto(
            location.getId(),
            location.getName(),
            location.getLat(),
            location.getLon(),
            location.getRadius());
    }

    public AdminLocation toEntity(NewAdminLocationDto dto) {
        AdminLocation location = new AdminLocation();
        location.setLat(dto.lat());
        location.setLon(dto.lon());
        location.setName(dto.name());
        location.setRadius(dto.radius());
        return location;
    }

    public AdminLocation toEntity(UpdateAdminLocationDto dto) {
        AdminLocation location = new AdminLocation();
        location.setLat(dto.lat());
        location.setLon(dto.lon());
        location.setName(dto.name());
        location.setRadius(dto.radius());
        return location;
    }
}
