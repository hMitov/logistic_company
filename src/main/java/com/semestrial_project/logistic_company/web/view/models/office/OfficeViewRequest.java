package com.semestrial_project.logistic_company.web.view.models.office;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OfficeViewRequest {

    @NotBlank
    private String city;

    @NotBlank
    private String street;

    @NotBlank
    private String streetNum;
}
