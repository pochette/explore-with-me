package ru.burdak.mainservice.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.burdak.mainservice.model.Category;

/**
 * DTO for {@link Category}
 */
public record CategoryDto(

    Long id,

    @Size(min = 1, max = 50)
    @NotBlank
    String name) {
}