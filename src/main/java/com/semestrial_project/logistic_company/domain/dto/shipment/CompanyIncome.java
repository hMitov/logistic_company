package com.semestrial_project.logistic_company.domain.dto.shipment;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompanyIncome {

    private LocalDate startDate;

    private LocalDate endDate;
}
