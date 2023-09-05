package com.semestrial_project.logistic_company.web.view.models.vehicle;

import lombok.Data;

@Data
public class VehicleViewResponse {

    private Long id;

    private String brand;

    private String model;

    private String regPlateNumber;

    private Long vehicleId;
}
