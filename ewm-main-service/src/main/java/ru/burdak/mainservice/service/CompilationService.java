package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.burdak.mainservice.dto.compilation.CompilationDto;
import ru.burdak.mainservice.dto.compilation.NewCompilationDto;
import ru.burdak.mainservice.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    void deleteCompilationByIdFromAdminController(HttpServletRequest httpRequest, Long compId);

    CompilationDto getCompilationByIdPublic(HttpServletRequest httpRequest, Long compId);

    List<CompilationDto> getCompilationsPublic(HttpServletRequest httpRequest, Boolean pinned, Integer from,
                                               Integer size);

    CompilationDto patchCompilationByIdFromAdminController(Long compId, UpdateCompilationRequest request);

    CompilationDto postNewCompilationFromAdminController(HttpServletRequest httpRequest, NewCompilationDto dto);
}
