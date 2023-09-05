package com.semestrial_project.logistic_company.web.view.models.sender;

import com.semestrial_project.logistic_company.web.view.models.address.AddressViewRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SenderViewRequest {

    private String firstName;

    private String lastName;

    private String organizationName;

    @NotBlank
    private String customerType;

    private AddressViewRequest address;

    @NotBlank
    private String telephone;

    @NotBlank
    private String email;

    private String specialInstructions;
}
