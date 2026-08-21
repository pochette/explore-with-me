package ru.burdak.statservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.burdak.ewmstatsclient.dto.ViewStatsDto;
import ru.burdak.statservice.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query(value = "" +
        "select  new ru.burdak.ewmstatsclient.dto.ViewStatsDto(" +
        "h.app.name," +
        "h.uri," +
        "count (h.id) " +
        ") " +
        "from Hit h " +
        "where h.timestamp between :start and :end " +
        "group by h.app.name, h.uri " +
        "order by count (h.id) desc ")
    List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end);

    @Query(value = "" +
        "SELECT NEW ru.burdak.ewmstatsclient.dto.ViewStatsDto(" +
        "h.app.name, " +
        "h.uri, " +
        "count (h.id)" +
        ") " +
        "from Hit h " +
        "where h.timestamp between :start and :end " +
        "and h.uri in :uris " +
        "group by h.app.name, h.uri " +
        "order by count (h.id) desc")
    List<ViewStatsDto> findStatsByUriAndDataRange(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "" +
        "select new ru.burdak.ewmstatsclient.dto.ViewStatsDto(" +
        "h.app.name," +
        "h.uri," +
        "count (distinct h.ip) " +
        ") " +
        "from Hit h " +
        "where h.timestamp between :start and :end " +
        "and h.uri in :uris " +
        "group by h.app.name, h.uri " +
        "order by count (distinct h.ip) desc")
    List<ViewStatsDto> findStatsByUrisUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "" +
        "select new ru.burdak.ewmstatsclient.dto.ViewStatsDto( " +
        "h.app.name, " +
        "h.uri, " +
        "count (distinct h.ip)" +
        ") " +
        "from Hit h " +
        "where h.timestamp between :start and :end " +
        "group by h.app.name, h.uri " +
        "order by count (distinct h.ip) desc")
    List<ViewStatsDto> findStatsUniqueIp(LocalDateTime start, LocalDateTime end);

}
