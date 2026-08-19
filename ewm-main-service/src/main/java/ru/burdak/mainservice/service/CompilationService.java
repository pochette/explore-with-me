package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.burdak.mainservice.dto.compilation.CompilationDto;
import ru.burdak.mainservice.dto.compilation.NewCompilationDto;
import ru.burdak.mainservice.dto.compilation.UpdateCompilationRequest;

public interface CompilationService {

    void deleteCompilationByIdFromAdminController(HttpServletRequest httpRequest, Long compId);

    CompilationDto patchCompilationByIdFromAdminController(Long compId, UpdateCompilationRequest request);

    CompilationDto postNewCompilationFromAdminController(HttpServletRequest httpRequest, NewCompilationDto dto);
}
