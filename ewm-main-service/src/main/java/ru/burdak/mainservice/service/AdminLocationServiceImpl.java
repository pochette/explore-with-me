package ru.burdak.mainservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.burdak.mainservice.dto.location.AdminLocationDto;
import ru.burdak.mainservice.dto.location.NewAdminLocationDto;
import ru.burdak.mainservice.dto.location.UpdateAdminLocationDto;
import ru.burdak.mainservice.exception.ConflictException;
import ru.burdak.mainservice.exception.NotFoundException;
import ru.burdak.mainservice.mapper.AdminLocationMapper;
import ru.burdak.mainservice.model.AdminLocation;
import ru.burdak.mainservice.repository.AdminLocationRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor

public class AdminLocationServiceImpl implements AdminLocationService {
    private final AdminLocationRepository adminLocationRepository;

    @Override
    @Transactional
    public AdminLocationDto addNewAdminLocation(NewAdminLocationDto dto) {
        if (adminLocationRepository.existsByName(dto.name())) {
            throw new ConflictException("Location with name=" + dto.name() + " already exists");

        }
        AdminLocation location = AdminLocationMapper.toEntity(dto);
        AdminLocation savedLocation = adminLocationRepository.save(location);
        log.debug("Location with id={} was created: {}", savedLocation.getId(), savedLocation);

        return AdminLocationMapper.toDto(savedLocation);
    }

    @Override
    public void deleteLocationById(Long locationId) {
        if (!adminLocationRepository.existsById(locationId)) {
            throw new NotFoundException("Location with id=" + locationId + " was not found");
        }
        adminLocationRepository.deleteById(locationId);
        log.debug("Location with id={} was deleted", locationId);
    }

    @Transactional(readOnly = true)
    @Override
    public AdminLocationDto getLocationById(Long locationId) {
        AdminLocation location = adminLocationRepository.findById(locationId).orElseThrow(
            () -> new NotFoundException("Location with id=" + locationId + " was not found")
        );
        log.debug("Location with id={} was found: {}", locationId, location);
        return AdminLocationMapper.toDto(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminLocationDto> getLocationsListByFilter(Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size,
            size, Sort.by("id").descending());
        Page<AdminLocation> locations = adminLocationRepository.findAll(pageable);
        log.debug("Locations list was found: {}", locations.getContent());

        return locations.map(AdminLocationMapper::toDto).getContent();
    }

    @Override
    public List<AdminLocationDto> getLocationsListByIdPublic(Long locationId) {
        List<AdminLocation> locations = adminLocationRepository.findAllById(List.of(locationId));
        if (locations.isEmpty()) {
            throw new NotFoundException("Location with id=" + locationId + " was not found");
        }
        log.debug("Locations list was found: {}", locations);
        log.info("Locations list with size {} was found", locations.size());
        return locations.stream().map(AdminLocationMapper::toDto).toList();
    }

    @Override
    public AdminLocationDto patchLocationById(Long locationId, UpdateAdminLocationDto dto) {
        AdminLocation location = adminLocationRepository.findById(locationId).orElseThrow(
            () -> new NotFoundException("Location with id=" + locationId + " was not found")
        );
        if (dto.name() != null) {
            if (adminLocationRepository.existsByName(dto.name())) {
                throw new ConflictException("Location with name=" + dto.name() + " already exists");
            }
            location.setName(dto.name());
        }
        if (dto.lat() != null) {
            location.setLat(dto.lat());
        }
        if (dto.lon() != null) {
            location.setLon(dto.lon());
        }
        if (dto.radius() != null) {
            location.setRadius(dto.radius());
        }
        AdminLocation updatedLocation = adminLocationRepository.save(location);
        log.debug("Location with id={} was updated: {}", locationId, updatedLocation);
        return AdminLocationMapper.toDto(updatedLocation);
    }

}
