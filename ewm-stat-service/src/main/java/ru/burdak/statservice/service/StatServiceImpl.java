package ru.burdak.statservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.burdak.statservice.dto.EndpointHitDto;
import ru.burdak.statservice.mapper.StatMapper;
import ru.burdak.statservice.model.App;
import ru.burdak.statservice.model.Hit;
import ru.burdak.statservice.repository.AppRepository;
import ru.burdak.statservice.repository.HitRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StatServiceImpl implements StatService {
    private final HitRepository hitRepository;
    private final AppRepository appRepository;

    @Override
    public EndpointHitDto postNewHit(EndpointHitDto dto) {
        App app = appRepository.findAppByName(dto.app())
            .orElseGet(() -> {
                App newApp = new App();
                newApp.setName(dto.app());
                return appRepository.save(newApp);
            });
        Hit hit = StatMapper.toHitEntity(dto, app);

        return StatMapper.toEndpointHitDto(hitRepository.save(hit));
    }
}
