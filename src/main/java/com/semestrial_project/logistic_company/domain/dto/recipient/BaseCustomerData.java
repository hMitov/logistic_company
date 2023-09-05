package com.semestrial_project.logistic_company.domain.dto.recipient;

import com.semestrial_project.logistic_company.domain.dto.address.AddressRequest;
import lombok.Data;

@Data
public class BaseCustomerData {

    private String firstName;

    private String lastName;

    private String organizationName;

    private String customerType;

    private AddressRequest address;

    private String telephone;

    private String email;

    private String specialInstructions;
}
