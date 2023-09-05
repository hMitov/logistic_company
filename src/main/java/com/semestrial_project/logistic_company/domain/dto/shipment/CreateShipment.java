package com.semestrial_project.logistic_company.domain.dto.shipment;

import com.semestrial_project.logistic_company.domain.dto.recipient.RecipientData;
import com.semestrial_project.logistic_company.domain.dto.sender.SenderData;
import lombok.Data;

@Data
public class CreateShipment {

    private String departurePoint;

    private String arrivalPoint;

    private String departureAddress;

    private String arrivalAddress;

    private Long registrantEmployeeId;

    private RecipientData recipient;

    private SenderData sender;
}
