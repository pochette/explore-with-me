package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.burdak.ewmstatsclient.client.StatsClient;
import ru.burdak.ewmstatsclient.dto.EndpointHitDto;
import ru.burdak.ewmstatsclient.dto.ViewStatsDto;
import ru.burdak.mainservice.dto.event.EventFullDto;
import ru.burdak.mainservice.exception.NotFoundException;
import ru.burdak.mainservice.model.*;
import ru.burdak.mainservice.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {
    @Mock
    private EventRepository eventRepository;

//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private CategoryRepository categoryRepository;

    @Mock
    private StatsClient statsClient;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event event;
    private User user;

    @BeforeEach
    void setUp() {
        event = new Event();

        event.setId(1L);
        event.setAnnotation("Музыкальное событие");
        event.setDescription("Подробное описание музыкального события");
        event.setTitle("Концерт");
        event.setState(EventState.PUBLISHED);
        event.setCategory(new Category(1L, "Music"));
        event.setCreatedOn(LocalDateTime.now().minusDays(1));
        event.setEventDate(LocalDateTime.now().plusDays(5));
        event.setPublishedOn(LocalDateTime.now().minusHours(12));

        event.setPaid(true);
        event.setParticipantLimit(10);
        event.setConfirmedRequests(1);
        event.setRequestModeration(true);
        event.setViews(1L);

        user = new User();
        user.setId(1L);
        user.setName("Иван Иванов");
        user.setEmail("ivan@example.com");
        event.setInitiator(user);

        Location location = new Location();
        location.setId(1L);
        location.setLat(55.7298f);
        location.setLon(37.6019f);
        event.setLocation(location);
    }

    @Test
    void shouldReturnEventByUserIdAndEventId() {
        when(eventRepository.findByInitiator_IdAndId(1L, 1L)).thenReturn(Optional.of(event));
        EventFullDto result = eventService.getEventByUserIdAndEventId(1L, 1L);

        assertThat(result.initiator().id()).isEqualTo(event.getInitiator().getId());
        assertThat(result.id()).isEqualTo(event.getId());
    }

    @Test
    void shouldReturnEventsByFilterAdmin() {
        List<Long> users = List.of(user.getId());
        List<EventState> states = List.of(EventState.PUBLISHED);
        List<Long> categories = List.of(event.getCategory().getId());

        LocalDateTime rangeStart = LocalDateTime.now().minusWeeks(2);
        LocalDateTime rangeEnd = LocalDateTime.now().plusWeeks(2);
        Integer from = 0;
        Integer size = 10;
        when(eventRepository.findAll(
            ArgumentMatchers.<Specification<Event>>any(),
            any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of(event)));

        when(request.getRequestURI())
            .thenReturn("/admin/events");

        when(request.getRemoteAddr())
            .thenReturn("127.0.0.1");

        List<EventFullDto> result =
            eventService.getEventsByFilterAdmin(users, states, categories, rangeStart, rangeEnd, from, size, request);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().views()).isEqualTo(1L);
        verify(statsClient).saveHit(any(EndpointHitDto.class));
    }

    @Test
    void shouldReturnPublishedEventAndSaveStatistics() {
        when(eventRepository.findByIdAndStateEquals(1L, EventState.PUBLISHED)).thenReturn(Optional.of(event));
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(request.getRequestURI()).thenReturn("/events/1");
        when(statsClient.getStats(
            any(LocalDateTime.class),
            any(LocalDateTime.class),
            eq(List.of("/events/1")),
            eq(true)))
            .thenReturn(List.of(new ViewStatsDto("ewm-main-service", "/events/1", 1L)));

        EventFullDto result = eventService.getEventByIdPublic(request, event.getId());
        assertThat(result.id()).isEqualTo(event.getId());
        assertThat(result.state()).isEqualTo(event.getState());
        assertThat(result.createdOn()).isEqualTo(event.getCreatedOn());
        assertThat(result.views()).isEqualTo(1L);

        verify(request).getRequestURI();
        verify(request).getRemoteAddr();

        verify(statsClient).saveHit(any(EndpointHitDto.class));
        verify(statsClient).getStats(
            any(LocalDateTime.class),
            any(LocalDateTime.class),
            eq(List.of("/events/1")),
            eq(true)
        );

    }

    @Test
    void shouldThrowWhenEventNotFound() {
        when(eventRepository.findByIdAndStateEquals(1L, EventState.PUBLISHED)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> eventService.getEventByIdPublic(request, event.getId()));

    }

    @Test
    void shouldThrowWhenEventNotFoundByUserIdAndEventId() {
        when(eventRepository.findByInitiator_IdAndId(1L, 1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> eventService.getEventByUserIdAndEventId(1L, 1L));
    }

}
