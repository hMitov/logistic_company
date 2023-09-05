package com.semestrial_project.logistic_company.domain.services.validator.annotations.annotations_validators;

import com.semestrial_project.logistic_company.domain.services.validator.annotations.ValidLong;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LongValidator implements ConstraintValidator<ValidLong, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
