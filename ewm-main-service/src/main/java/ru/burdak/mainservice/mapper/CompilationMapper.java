package ru.burdak.mainservice.mapper;

import lombok.experimental.UtilityClass;
import ru.burdak.mainservice.dto.compilation.CompilationDto;
import ru.burdak.mainservice.dto.compilation.NewCompilationDto;
import ru.burdak.mainservice.model.Compilation;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * The type Compilation mapper.
 */
@UtilityClass
public class CompilationMapper {
    /**
     * To dto compilation dto.
     *
     * @param compilation the compilation
     * @return the compilation dto
     */
    public CompilationDto toDto(Compilation compilation) {
        return new CompilationDto(
            compilation.getEvents().stream().map(EventMapper::toShortDto).collect(Collectors.toSet()),
            compilation.getId(),
            compilation.getPinned(),
            compilation.getTitle()
        );
    }

    /**
     * To entity compilation.
     *
     * @param dto the dto
     * @return the compilation
     */
    public Compilation toEntity(NewCompilationDto dto) {
        Compilation compilation = new Compilation();
        compilation.setEvents(new HashSet<>());
        compilation.setPinned(Boolean.TRUE.equals(dto.pinned()));
        compilation.setTitle(dto.title());
        return compilation;
    }
}
