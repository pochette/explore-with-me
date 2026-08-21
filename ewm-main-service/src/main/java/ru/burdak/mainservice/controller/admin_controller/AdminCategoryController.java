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

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(HttpServletRequest request,
                               @PathVariable(name = "catId") @Positive Long catId) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        log.info("Получен запрос на удаление категории {} ", catId);
        categoryService.deleteCategory(request, catId);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> patchCategoryDto(HttpServletRequest request,
                                                        @PathVariable(name = "catId") @Positive Long catId,
                                                        @RequestBody @Valid CategoryDto categoryDto) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        log.info("Получен запрос на изменение категории с id {}", catId);
        log.info("Новые данные категории: {}", categoryDto);
        return new ResponseEntity<>(
            categoryService.patchCategoryAdmin(request, catId, categoryDto),
            HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<CategoryDto> postNewCategory(HttpServletRequest request,
                                                       @RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        log.info("Получен запрос на добавление новой категории: {}", newCategoryDto);

        return new ResponseEntity<>(
            categoryService.postNewCategory(request, newCategoryDto),
            HttpStatus.CREATED);
    }
}
