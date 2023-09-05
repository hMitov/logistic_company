package com.semestrial_project.logistic_company.web.view.models.office_employee;

import com.semestrial_project.logistic_company.domain.services.validator.annotations.ValidDouble;
import com.semestrial_project.logistic_company.domain.services.validator.annotations.ValidLocalDate;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class CreateOfficeEmployeeView {

    @NotBlank
    private String egn;

    @NotBlank
    private String firstName;

    @NotBlank
    private String middleName;

    @NotBlank
    private String lastName;

    @ValidDouble(message = "Invalid Double value, please enter valid number")
    private String salary;

    @ValidLocalDate(message = "Invalid date format. Please use yyyy-MM-dd.")
    private String dateOfEmploy;

    @NotBlank
    private String officeAddress;
}
