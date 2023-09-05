package com.semestrial_project.logistic_company.web.view.models.recipient;

import com.semestrial_project.logistic_company.web.view.models.address.AddressViewRequest;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class RecipientViewRequest {

    private String firstName;

    private String lastName;

    private String organizationName;

    @NotBlank
    private String customerType;

    @Valid
    private AddressViewRequest address;

    @NotBlank
    private String telephone;

    @NotBlank
    private String email;

    private String specialInstructions;
}
