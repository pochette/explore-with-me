package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import ru.burdak.mainservice.dto.category.CategoryDto;
import ru.burdak.mainservice.dto.category.NewCategoryDto;

public interface CategoryService {

    CategoryDto patchCategoryAdmin(HttpServletRequest request, Long catId, CategoryDto categoryDto);

    CategoryDto postNewCategory(HttpServletRequest request, NewCategoryDto newCategoryDto);
}
