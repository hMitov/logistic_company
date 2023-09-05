package com.semestrial_project.logistic_company.domain.services.validator.annotations;

import com.semestrial_project.logistic_company.domain.services.validator.annotations.annotations_validators.LongValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LongValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "Employee ID cannot be null")
public @interface ValidLong {
    String message() default "Invalid Long value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
