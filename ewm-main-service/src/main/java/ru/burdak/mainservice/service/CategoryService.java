package ru.burdak.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.burdak.mainservice.dto.category.CategoryDto;
import ru.burdak.mainservice.dto.category.NewCategoryDto;

import java.util.Collection;

public interface CategoryService {

    void deleteCategory(HttpServletRequest request, Long catId);

    Collection<CategoryDto> getCategoriesPublic(HttpServletRequest httpRequest, Integer from, Integer size);

    CategoryDto getCategoryByIdPublic(HttpServletRequest httpRequest, Long catId);

    CategoryDto patchCategoryAdmin(HttpServletRequest request, Long catId, CategoryDto categoryDto);

    CategoryDto postNewCategory(HttpServletRequest request, NewCategoryDto newCategoryDto);
}
