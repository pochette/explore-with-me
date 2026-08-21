package ru.burdak.mainservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.burdak.mainservice.repository.AdminLocationRepository;
import ru.burdak.mainservice.repository.LocationRepository;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor

public class AdminLocationServiceImpl implements AdminLocationService {
    private final AdminLocationRepository adminLocationRepository;
    private final LocationRepository locationRepository;

}
