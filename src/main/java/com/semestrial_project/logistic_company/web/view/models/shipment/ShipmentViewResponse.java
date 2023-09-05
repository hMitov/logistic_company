package com.semestrial_project.logistic_company.web.view.models.shipment;

import com.semestrial_project.logistic_company.web.view.models.address.AddressViewResponse;
import com.semestrial_project.logistic_company.web.view.models.recipient.RecipientViewResponse;
import com.semestrial_project.logistic_company.web.view.models.sender.SenderViewResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShipmentViewResponse {

    private Long id;

    private String externalId;

    private SenderViewResponse sender;

    private RecipientViewResponse recipient;

    private String departurePoint;

    private String arrivalPoint;

    private AddressViewResponse departureAddress;

    private AddressViewResponse arrivalAddress;

    private Double weight;

    private Double price;

    private Long registrantEmployeeId;

    private Long processedByEmployeeId;

    private String shipmentState;

    private LocalDateTime deliveredDateTime;

}
