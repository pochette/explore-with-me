package ru.burdak.ewmstatsclient.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.burdak.ewmstatsclient.dto.EndpointHitDto;
import ru.burdak.ewmstatsclient.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
public class StatsClient {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    private final RestTemplate restTemplate;
    private final String statsServerUrl;

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(statsServerUrl + "/stats")
            .queryParam("start", start.format(DATE_TIME_FORMATTER))
            .queryParam("end", end.format(DATE_TIME_FORMATTER))
            .queryParam("unique", unique);

        if (uris != null && !uris.isEmpty()) {
            builder.queryParam("uris", uris.toArray());
        }

        return restTemplate.exchange(
            builder.encode().build().toUri(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ViewStatsDto>>() {
            }
        ).getBody();
    }

    public void saveHit(EndpointHitDto dto) {
        restTemplate.postForEntity(statsServerUrl + "/hit", dto, EndpointHitDto.class);
    }

}
