package com.semestrial_project.logistic_company.domain.dto.sender;

import com.semestrial_project.logistic_company.domain.entity.Address;
import com.semestrial_project.logistic_company.domain.entity.enums.CUSTOMER_TYPE;
import lombok.Data;

import java.util.List;

@Data
public class SenderResponse {

    protected Long id;

    protected String firstName;

    protected String lastName;

    protected String organizationName;

    protected CUSTOMER_TYPE customerType;

    protected List<Address> addresses;

    protected String telephone;

    protected String email;

    private String specialInstructions;
}
