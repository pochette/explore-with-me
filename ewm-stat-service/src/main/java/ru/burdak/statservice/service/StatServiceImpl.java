package ru.burdak.statservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.burdak.ewmstatsclient.dto.EndpointHitDto;
import ru.burdak.ewmstatsclient.dto.ViewStatsDto;
import ru.burdak.statservice.mapper.StatMapper;
import ru.burdak.statservice.model.App;
import ru.burdak.statservice.model.Hit;
import ru.burdak.statservice.repository.AppRepository;
import ru.burdak.statservice.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StatServiceImpl implements StatService {
    private final HitRepository hitRepository;
    private final AppRepository appRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        boolean hasUris = uris != null && !uris.isEmpty();
        boolean onlyUnique = Boolean.TRUE.equals(unique);

        if (hasUris && onlyUnique) {
            return hitRepository.findStatsByUrisUnique(start, end, uris);
        }

        if (hasUris) {
            return hitRepository.findStatsByUriAndDataRange(start, end, uris);
        }

        if (onlyUnique) {
            return hitRepository.findStatsUniqueIp(start, end);
        }

        return hitRepository.findStats(start, end);
    }

    @Override
    @Transactional
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
