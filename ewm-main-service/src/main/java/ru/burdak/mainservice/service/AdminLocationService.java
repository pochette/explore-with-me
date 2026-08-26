package ru.burdak.mainservice.service;

import ru.burdak.mainservice.dto.location.AdminLocationDto;
import ru.burdak.mainservice.dto.location.NewAdminLocationDto;
import ru.burdak.mainservice.dto.location.UpdateAdminLocationDto;

import java.util.List;

public interface AdminLocationService {
    AdminLocationDto addNewAdminLocation(NewAdminLocationDto dto);

    void deleteLocationById(Long locationId);

    AdminLocationDto getLocationById(Long locationId);

    List<AdminLocationDto> getLocationsListByFilter(Integer from, Integer size);

    List<AdminLocationDto> getLocationsListByIdPublic(Long locationId);

    AdminLocationDto patchLocationById(Long locationId, UpdateAdminLocationDto dto);
}
