package ru.burdak.mainservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class EventDateAfterHoursValidator implements ConstraintValidator<EventDateAfterHours, LocalDateTime> {
    private int hours;

    @Override
    public void initialize(EventDateAfterHours constraintAnnotation) {
        this.hours = constraintAnnotation.hours();
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return !value.isBefore(LocalDateTime
            .now()
            .plusHours(hours));
    }
}
