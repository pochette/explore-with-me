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
import ru.burdak.mainservice.exception.ConflictException;
import ru.burdak.mainservice.mapper.AdminLocationMapper;
import ru.burdak.mainservice.model.AdminLocation;
import ru.burdak.mainservice.repository.AdminLocationRepository;
import ru.burdak.mainservice.repository.LocationRepository;

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

        return AdminLocationMapper.toDto(savedLocation);
    }

    @Override
    public List<AdminLocationDto> getLocationsListByFilter(Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size,
            size, Sort.by("id").descending());
        Page<AdminLocation> locations = adminLocationRepository.findAll(pageable);

        return locations.map(AdminLocationMapper::toDto).getContent();
    }

}
