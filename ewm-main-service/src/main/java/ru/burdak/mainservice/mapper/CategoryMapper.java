package ru.burdak.mainservice.mapper;

import ru.burdak.mainservice.dto.category.CategoryDto;
import ru.burdak.mainservice.dto.category.NewCategoryDto;
import ru.burdak.mainservice.model.Category;

public class CategoryMapper {

    public static Category toEntity(NewCategoryDto dto) {
        Category category = new Category();
        category.setName(dto.name());
        return category;
    }

    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

}
