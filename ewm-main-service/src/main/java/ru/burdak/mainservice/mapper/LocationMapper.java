package ru.burdak.mainservice.mapper;

import lombok.experimental.UtilityClass;
import ru.burdak.mainservice.dto.location.LocationDto;
import ru.burdak.mainservice.model.Location;

@UtilityClass
public class LocationMapper {
    public LocationDto toDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }

    public Location toEntity(LocationDto dto) {
        Location location = new Location();
        location.setLat(dto.lat());
        location.setLon(dto.lon());
        return location;
    }

}
