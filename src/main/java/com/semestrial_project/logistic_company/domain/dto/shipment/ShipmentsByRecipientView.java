package com.semestrial_project.logistic_company.domain.dto.shipment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ShipmentsByRecipientView {

    @NotBlank
    private String telephone;
}
