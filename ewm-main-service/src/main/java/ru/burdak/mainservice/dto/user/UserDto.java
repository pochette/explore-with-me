package ru.burdak.mainservice.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
    Long id,

    @Email
    @NotBlank
    String email,

    @NotBlank
    String name) {
}
