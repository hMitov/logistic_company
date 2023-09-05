package com.semestrial_project.logistic_company.domain.services.validator.annotations.annotations_validators;

import com.semestrial_project.logistic_company.domain.services.validator.annotations.ValidDouble;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DoubleValidator implements ConstraintValidator<ValidDouble, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
