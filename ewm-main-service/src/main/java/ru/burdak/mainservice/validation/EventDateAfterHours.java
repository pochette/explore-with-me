package ru.burdak.mainservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.time.LocalDateTime;

@Documented
@Constraint(validatedBy = EventDateAfterHoursValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventDateAfterHours {
    String message() default "Event date must be at least {hours} hours after now";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int hours();

}

