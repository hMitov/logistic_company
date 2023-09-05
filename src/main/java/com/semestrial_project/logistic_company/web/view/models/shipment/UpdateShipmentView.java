package com.semestrial_project.logistic_company.web.view.models.shipment;

import com.semestrial_project.logistic_company.web.view.models.sender.SenderViewRequest;
import lombok.Data;

@Data
public class UpdateShipmentView {

    private SenderViewRequest sender;

    private SenderViewRequest recipient;

    private String departurePoint;

    private String arrivalPoint;

    private String departureAddress;

    private String arrivalAddress;

    private Long registrantEmployeeId;

}
