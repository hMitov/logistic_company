package com.semestrial_project.logistic_company.domain.dto.shipment;

import lombok.Data;

@Data
public class ProcessShipment {

    private Double weight;

    private Long processedByEmployeeId;
}
