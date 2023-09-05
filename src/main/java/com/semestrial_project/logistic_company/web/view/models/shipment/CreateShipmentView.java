package com.semestrial_project.logistic_company.web.view.models.shipment;

import com.semestrial_project.logistic_company.web.view.models.recipient.RecipientViewRequest;
import com.semestrial_project.logistic_company.web.view.models.sender.SenderViewRequest;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class CreateShipmentView {

    @Valid
    private SenderViewRequest sender;

    @Valid
    private RecipientViewRequest recipient;

    @NotBlank
    private String departurePoint;

    @NotBlank
    private String arrivalPoint;

    private String departureAddress;

    private String arrivalAddress;

    private Long registrantEmployeeId;
}
