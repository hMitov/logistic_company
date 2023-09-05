package com.semestrial_project.logistic_company.domain.services.validator.annotations;

import com.semestrial_project.logistic_company.domain.services.validator.annotations.annotations_validators.DoubleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DoubleValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "Salary cannot be null")
public @interface ValidDouble {
    String message() default "Invalid Double value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
