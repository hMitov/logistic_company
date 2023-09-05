package com.semestrial_project.logistic_company.domain.dto.shipment;

import com.semestrial_project.logistic_company.domain.services.validator.annotations.ValidLong;
import lombok.Data;

@Data
public class ShipmentsByEmployeeView {

    @ValidLong(message = "Invalid Long value, please enter valid number")
    private String employeeId;
}
