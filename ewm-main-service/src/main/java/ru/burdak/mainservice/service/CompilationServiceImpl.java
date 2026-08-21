package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.burdak.mainservice.dto.compilation.CompilationDto;
import ru.burdak.mainservice.dto.compilation.NewCompilationDto;
import ru.burdak.mainservice.dto.compilation.UpdateCompilationRequest;
import ru.burdak.mainservice.exception.NotFoundException;
import ru.burdak.mainservice.mapper.CompilationMapper;
import ru.burdak.mainservice.model.Compilation;
import ru.burdak.mainservice.model.Event;
import ru.burdak.mainservice.repository.CompilationRepository;
import ru.burdak.mainservice.repository.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public void deleteCompilationByIdFromAdminController(HttpServletRequest httpRequest, Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
            () -> new NotFoundException("Compilation with id=" + compId + " was not found")
        );
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto getCompilationByIdPublic(HttpServletRequest httpRequest, Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
            () -> new NotFoundException("Compilation with id=" + compId + " was not found")
        );

        return CompilationMapper.toDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilationsPublic(HttpServletRequest httpRequest, Boolean pinned, Integer from,
                                                      Integer size) {
        Pageable pageable = PageRequest.of(from / size,
            size);
        if (pinned != null) {
            return compilationRepository.findAllByPinned(pinned, pageable)
                .stream()
                .map(CompilationMapper::toDto)
                .toList();
        } else {
            return compilationRepository.findAll(pageable)
                .stream()
                .map(CompilationMapper::toDto)
                .toList();
        }
    }

    @Override
    @Transactional
    public CompilationDto patchCompilationByIdFromAdminController(Long compId, UpdateCompilationRequest request) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
            () -> new NotFoundException("Compilation with id=" + compId + "was not found")
        );
        if (request.events() != null) {
            Set<Event> events = eventRepository.findAllByIdIn(request.events());
            if (events.size() != request.events().size()) {
                throw new NotFoundException("Events with ids=" + request.events() + "were not found");
            }
            compilation.setEvents(events);
        }
        updateIfPresent(request.pinned(), compilation::setPinned);
        updateIfPresent(request.title(), compilation::setTitle);

        compilationRepository.save(compilation);
        return CompilationMapper.toDto(compilation);
    }

    @Override
    @Transactional
    public CompilationDto postNewCompilationFromAdminController(HttpServletRequest httpRequest, NewCompilationDto dto) {
        Set<Event> events = dto.events() == null ? new HashSet<>() : eventRepository.findAllByIdIn(dto.events());

        Compilation compilation = CompilationMapper.toEntity(dto);
        compilation.setEvents(events);

        Compilation savedCompilation = compilationRepository.save(compilation);
        return CompilationMapper.toDto(savedCompilation);
    }

    private <T> void updateIfPresent(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
