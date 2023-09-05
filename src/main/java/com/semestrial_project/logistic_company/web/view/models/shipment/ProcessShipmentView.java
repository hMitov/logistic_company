package com.semestrial_project.logistic_company.web.view.models.shipment;

import com.semestrial_project.logistic_company.domain.services.validator.annotations.ValidLong;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProcessShipmentView {

    @NotNull
    private Double weight;

    @ValidLong(message = "Invalid Long value, please enter valid number")
    private String processedByEmployeeId;

}
