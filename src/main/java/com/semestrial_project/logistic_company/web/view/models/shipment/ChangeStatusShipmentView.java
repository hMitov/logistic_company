package com.semestrial_project.logistic_company.web.view.models.shipment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangeStatusShipmentView {

    @NotBlank
    private String shipmentStatus;
}
