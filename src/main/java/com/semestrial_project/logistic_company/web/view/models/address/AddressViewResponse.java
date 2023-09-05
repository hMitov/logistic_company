package com.semestrial_project.logistic_company.web.view.models.address;

import lombok.Data;

@Data
public class AddressViewResponse {

    private Long id;

    private String city;

    private String postCode;

    private String street;

    private String streetNum;
}
