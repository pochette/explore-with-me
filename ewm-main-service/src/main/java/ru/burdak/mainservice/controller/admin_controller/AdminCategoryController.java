package ru.burdak.mainservice.controller.admin_controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.burdak.mainservice.dto.category.CategoryDto;
import ru.burdak.mainservice.dto.category.NewCategoryDto;
import ru.burdak.mainservice.service.CategoryService;

@RestController
@RequestMapping(("/admin/categories"))
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> postNewCategory(HttpServletRequest request,
                                                       @RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ResponseEntity<>(
            categoryService.postNewCategory(request, newCategoryDto),
            HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> patchCategoryDto(HttpServletRequest request,
        @PathVariable(name = "catId", required = true) @Positive Long catId,
        @RequestBody @Valid CategoryDto categoryDto) {
        return new ResponseEntity<>(
            categoryService.patchCategoryAdmin(request, catId, categoryDto),
            HttpStatus.OK);

    }
}
