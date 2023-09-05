package com.semestrial_project.logistic_company.web.view.models.office;

import com.semestrial_project.logistic_company.domain.services.validator.annotations.ValidLocalDate;
import com.semestrial_project.logistic_company.web.view.models.address.AddressViewRequest;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class CreateOfficeView {

    @Valid
    private AddressViewRequest officeAddress;

    @NotBlank
    private String telephone;

    @NotBlank
    @ValidLocalDate(message = "Invalid date format. Please use yyyy-MM-dd.")
    private String dateOfEstablishment;
}
