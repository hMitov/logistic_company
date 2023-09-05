package com.semestrial_project.logistic_company.domain.dto.vehicle;

import lombok.Data;

@Data
public class VehicleResponse {

    private Long id;

    private String brand;

    private String model;

    private String regPlateNumber;

    private Long vehicleId;

}
