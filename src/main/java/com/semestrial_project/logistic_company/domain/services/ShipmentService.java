package com.semestrial_project.logistic_company.domain.services;

import com.semestrial_project.logistic_company.domain.dto.shipment.*;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;

import java.util.List;

public interface ShipmentService {

    ShipmentResponse createShipment(CreateShipment shipmentData) throws DomainException;

    ShipmentResponse getShipmentById(Long id) throws DomainException;

    List<ShipmentResponse> getAllShipments();

    List<ShipmentResponse> getTransportReadyShipments();

    void processShipment(Long id, ProcessShipment convertToProcessShipment) throws DomainException;

    ShipmentResponse updateShipment(Long id, UpdateShipment updateShipment) throws DomainException;

    void changeShipmentTransportState(Long id, ChangeStatusShipment changeStatusShipment) throws DomainException;

    List<ShipmentStateResponse> getAllShipmentStates();

    List<ShipmentResponse> getAllShipmentsInTransport();

    List<ShipmentResponse> getAllShipmentsInTransportToRecipient();

    List<ShipmentResponse> getAllShipmentsByRegistrantEmployeeId(Long employeeId);

    List<ShipmentResponse> getAllShipmentsSentBySender(String telephone);

    List<ShipmentResponse> getAllShipmentsReceivedByRecipient(String telephone);

    double getIncomeFromShipments(CompanyIncome companyIncome) throws DomainException;

    List<ShipmentResponse> getAllShipmentsForProcessing();

    void removeShipment(Long id) throws DomainException;
}
