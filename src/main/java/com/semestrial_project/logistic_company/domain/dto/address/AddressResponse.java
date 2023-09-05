package com.semestrial_project.logistic_company.domain.dto.address;

import lombok.Data;

@Data
public class AddressResponse {

    private Long id;

    private String city;

    private String postCode;

    private String street;

    private String streetNum;
}
