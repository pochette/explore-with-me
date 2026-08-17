package ru.burdak.mainservice.service;

import jakarta.persistence.Table;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.burdak.mainservice.dto.category.CategoryDto;
import ru.burdak.mainservice.dto.category.NewCategoryDto;
import ru.burdak.mainservice.exception.ConflictException;
import ru.burdak.mainservice.exception.NotFoundException;
import ru.burdak.mainservice.mapper.CategoryMapper;
import ru.burdak.mainservice.model.Category;
import ru.burdak.mainservice.repository.CategoryRepository;
import ru.burdak.mainservice.repository.EventRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public void deleteCategory(HttpServletRequest request, Long catId) {
        if (!categoryRepository.existsById((catId))) {
            throw new NotFoundException("Category with id " + catId + "  was not found");
        }
        if (eventRepository.existsByCategory_Id(catId)) {
            throw new ConflictException("The category is not empty");
        }

        categoryRepository.deleteById(catId);



    }

    @Transactional
    @Override
    public CategoryDto patchCategoryAdmin(HttpServletRequest request, Long catId, CategoryDto categoryDto) {
        Category categoryForUpdate = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException(
            "Category with id= " + catId + " was not found"));

        if (categoryRepository.existsByNameAndIdNot(categoryDto.name(), catId)) {
            throw new ConflictException("Category with name " + categoryDto.name() + " is already exists");
        }

        categoryForUpdate.setName(categoryDto.name());

        log.info("Изменена категория : {} " , categoryForUpdate);
        return CategoryMapper.toDto(categoryRepository.save(categoryForUpdate));
    }

    @Transactional
    @Override
    public CategoryDto postNewCategory(HttpServletRequest request, NewCategoryDto newCategoryDto) {
        if (categoryRepository.existsByName(newCategoryDto.name())) {
            throw new ConflictException("Category with name " + newCategoryDto.name() + " is already exists");
        }

        Category category = CategoryMapper.toEntity(newCategoryDto);
        Category savedCategory = categoryRepository.save(category);

        return CategoryMapper.toDto(savedCategory);
    }

}
