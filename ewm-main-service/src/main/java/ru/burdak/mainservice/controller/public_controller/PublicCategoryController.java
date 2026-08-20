package ru.burdak.mainservice.controller.public_controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.burdak.mainservice.dto.category.CategoryDto;
import ru.burdak.mainservice.service.CategoryService;

import java.util.Collection;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Collection<CategoryDto>> getCategoriesPublic(
        HttpServletRequest httpRequest,
        @RequestParam(name = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
        @RequestParam(name = "size", required = false, defaultValue = "10") @PositiveOrZero Integer size) {
        log.info("{} {}?{}", httpRequest.getMethod(), httpRequest.getRequestURI(), httpRequest.getQueryString());
        return new ResponseEntity<>(categoryService.getCategoriesPublic(httpRequest, from, size),
            HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategoryByIdPublic(
        HttpServletRequest httpRequest,
        @PathVariable(name = "catId") @Positive Long catId) {
        log.info("{} {}?{}", httpRequest.getMethod(), httpRequest.getRequestURI(), httpRequest.getQueryString());
        return new ResponseEntity<>(categoryService.getCategoryByIdPublic(httpRequest, catId),
            HttpStatus.OK);
    }

}
