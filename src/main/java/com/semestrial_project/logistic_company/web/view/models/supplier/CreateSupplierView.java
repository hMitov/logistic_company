package com.semestrial_project.logistic_company.web.view.models.supplier;

import com.semestrial_project.logistic_company.domain.services.validator.annotations.ValidDouble;
import com.semestrial_project.logistic_company.domain.services.validator.annotations.ValidLocalDate;
import com.semestrial_project.logistic_company.domain.services.validator.annotations.ValidLong;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
public class CreateSupplierView {

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

    @NotNull
    private Set<String> drivingLicenseCategories;

    @ValidLong(message = "Invalid Long value, please enter valid number")
    private String vehicleId;
}
