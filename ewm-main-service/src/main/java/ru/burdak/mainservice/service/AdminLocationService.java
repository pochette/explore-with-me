package ru.burdak.mainservice.service;

import ru.burdak.mainservice.dto.location.AdminLocationDto;
import ru.burdak.mainservice.dto.location.NewAdminLocationDto;

import java.util.List;

public interface AdminLocationService {
    AdminLocationDto addNewAdminLocation(NewAdminLocationDto dto);

    List<AdminLocationDto> getLocationsListByFilter(Integer from, Integer size);
}
