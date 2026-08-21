package ru.burdak.mainservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventDateAfterHoursValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventDateAfterHours {
    Class<?>[] groups() default {};

    int hours();

    String message() default "Event date must be at least {hours} hours after now";

    Class<? extends Payload>[] payload() default {};

}

