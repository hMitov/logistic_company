package com.semestrial_project.logistic_company.web.view.models.office;

import com.semestrial_project.logistic_company.web.view.models.address.AddressViewRequest;
import lombok.Data;

@Data
public class UpdateOfficeView {

    private AddressViewRequest officeAddress;

    private String telephone;

    private String dateOfEstablishment;
}
