package ru.burdak.mainservice.dto.location;

import jakarta.validation.constraints.*;

public record NewAdminLocationDto(
    @NotBlank(message = "Название локации не должно быть пустым")
    @Size(max = 255, message = "Название локации не должно превышать 255 символов")
    String name,

    @NotNull(message = "Широта обязательна")
    @DecimalMin(value = "-90.0", message = "Широта не может быть меньше -90")
    @DecimalMax(value = "90.0", message = "Широта не может быть больше 90")
    Float lat,

    @NotNull(message = "Долгота обязательна")
    @DecimalMin(value = "-180.0", message = "Долгота не может быть меньше -180")
    @DecimalMax(value = "180.0", message = "Долгота не может быть больше 180")
    Float lon,

    @NotNull(message = "Радиус обязателен")
    @DecimalMin(value = "0.0", inclusive = false, message = "Радиус должен быть больше нуля")
    Double radius
) {
}
