package com.semestrial_project.logistic_company.web.view.models.address;

import lombok.Data;

@Data
public class AddressViewRequest {

    private String city;

    private String postCode;

    private String street;

    private String streetNum;
}
