package com.semestrial_project.logistic_company.web.view.models.vehicle;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateVehicleView {

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotBlank
    private String regPlateNumber;
}
