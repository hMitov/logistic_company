package com.semestrial_project.logistic_company.domain.services.validator.annotations.annotations_validators;

import com.semestrial_project.logistic_company.domain.services.validator.annotations.ValidLocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateValidator implements ConstraintValidator<ValidLocalDate, String> {


    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if (date == null) {
            return true; // Let @NotNull handle null values
        }

        try {
            LocalDate localDate = LocalDate.parse(date);
            localDate.format(DateTimeFormatter.ISO_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
