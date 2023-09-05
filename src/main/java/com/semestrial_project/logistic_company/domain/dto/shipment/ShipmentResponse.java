package com.semestrial_project.logistic_company.domain.dto.shipment;

import com.semestrial_project.logistic_company.domain.dto.address.AddressResponse;
import com.semestrial_project.logistic_company.domain.dto.recipient.RecipientRs;
import com.semestrial_project.logistic_company.domain.dto.sender.SenderRs;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShipmentResponse {

    private Long id;

    private String externalId;

    private SenderRs sender;

    private RecipientRs recipient;

    private String departurePoint;

    private String arrivalPoint;

    private AddressResponse departureAddress;

    private AddressResponse arrivalAddress;

    private Double weight;

    private Double price;

    private Long registrantEmployeeId;

    private Long processedByEmployeeId;

    private String shipmentState;

    private LocalDateTime deliveredDateTime;

}
