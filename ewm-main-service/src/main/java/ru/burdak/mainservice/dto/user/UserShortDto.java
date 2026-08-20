    package ru.burdak.mainservice.dto.user;

    import jakarta.validation.constraints.NotBlank;

    public record UserShortDto(
        Long id,

        @NotBlank
        String name) {

    }