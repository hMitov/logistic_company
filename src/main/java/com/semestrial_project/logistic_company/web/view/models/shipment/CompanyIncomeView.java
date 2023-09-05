package com.semestrial_project.logistic_company.web.view.models.shipment;

import com.semestrial_project.logistic_company.domain.services.validator.annotations.ValidLocalDate;
import lombok.Data;

@Data
public class CompanyIncomeView {

    @ValidLocalDate(message = "Invalid date format. Please use yyyy-MM-dd.")
    private String startDate;

    @ValidLocalDate(message = "Invalid date format. Please use yyyy-MM-dd.")
    private String endDate;
}
