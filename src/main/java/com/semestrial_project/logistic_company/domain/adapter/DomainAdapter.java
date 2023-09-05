package com.semestrial_project.logistic_company.domain.adapter;

import com.semestrial_project.logistic_company.domain.dto.address.AddressResponse;
import com.semestrial_project.logistic_company.domain.dto.driving_category.DrivingCategoryResponse;
import com.semestrial_project.logistic_company.domain.dto.office.OfficeResponse;
import com.semestrial_project.logistic_company.domain.dto.office_employee.OfficeEmployeeResponse;
import com.semestrial_project.logistic_company.domain.dto.recipient.RecipientResponse;
import com.semestrial_project.logistic_company.domain.dto.recipient.RecipientRs;
import com.semestrial_project.logistic_company.domain.dto.sender.SenderResponse;
import com.semestrial_project.logistic_company.domain.dto.sender.SenderRs;
import com.semestrial_project.logistic_company.domain.dto.shipment.ShipmentResponse;
import com.semestrial_project.logistic_company.domain.dto.shipment.ShipmentStateResponse;
import com.semestrial_project.logistic_company.domain.dto.supplier.SupplierResponse;
import com.semestrial_project.logistic_company.domain.dto.vehicle.VehicleResponse;
import com.semestrial_project.logistic_company.domain.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface DomainAdapter {

    @Mappings({
            @Mapping(target = "id", source = "office.id"),
            @Mapping(target = "officeAddress", source = "address")
    })
    OfficeResponse convertToOfficeResponse(Office office, AddressResponse address);

    @Mappings({
            @Mapping(target = "id", source = "recipient.id"),
            @Mapping(target = "address", source = "address")
    })
    RecipientRs convertToRecipientRs(Recipient recipient, AddressResponse address);

    @Mappings({
            @Mapping(target = "id", source = "sender.id"),
            @Mapping(target = "address", source = "address")
    })
    SenderRs convertToSenderRs(Sender sender, AddressResponse address);

    @Mappings({
            @Mapping(target = "id", source = "shipment.id"),
            @Mapping(target = "sender.address", ignore = true),
            @Mapping(target = "recipient.address", ignore = true),
            @Mapping(target = "registrantEmployeeId", source = "shipment.registrant.employeeId"),
            @Mapping(target = "processedByEmployeeId", source = "shipment.processedByEmployee.employeeId"),
            @Mapping(target = "shipmentState", source = "shipment.shipmentState.stateName"),
    })
    ShipmentResponse convertToShipmentResponse(Shipment shipment);

//    @Mappings({
//            @Mapping(target = "id", source = "shipment.id"),
//            @Mapping(target = "externalId", source = "shipment.externalId"),
//            @Mapping(target = "sender.id", source = "shipment.sender.id"),
//            @Mapping(target = "sender.firstName", source = "shipment.sender.firstName"),
//            @Mapping(target = "sender.lastName", source = "shipment.sender.lastName"),
//            @Mapping(target = "sender.address", source = "shipment.sender.customerAddress"),
//            @Mapping(target = "sender.telephone", source = "shipment.sender.telephone"),
//            @Mapping(target = "sender.email", source = "shipment.sender.email"),
//            @Mapping(target = "recipient.id", source = "shipment.recipient.id"),
//            @Mapping(target = "recipient.firstName", source = "shipment.recipient.firstName"),
//            @Mapping(target = "recipient.lastName", source = "shipment.recipient.lastName"),
//            @Mapping(target = "recipient.address", source = "shipment.recipient.customerAddress"),
//            @Mapping(target = "recipient.telephone", source = "shipment.recipient.telephone"),
//            @Mapping(target = "recipient.email", source = "shipment.recipient.email"),
//            @Mapping(target = "fromOfficeOrAddress", source = "shipment.fromOfficeOrAddress"),
//            @Mapping(target = "toOfficeOrAddress", source = "shipment.toOfficeOrAddress"),
//            @Mapping(target = "departureAddress", source = "shipment.departureAddress"),
//            @Mapping(target = "deliveryAddress", source = "shipment.deliveryAddress"),
//            @Mapping(target = "weight", source = "shipment.weight"),
//            @Mapping(target = "registrantEmployeeId", source = "shipment.registrant.id"),
//            @Mapping(target = "processedByEmployeeId", source = "shipment.processedByEmployee.id"),
//            @Mapping(target = "shipmentState", source = "shipment.shipmentState.stateName"),
//            @Mapping(target = "deliveredDateTime", source = "shipment.deliveredDateTime")
//    })
//    ShipmentResponse convertToShipmentResponseAll(Shipment shipment);

    @Mappings({
            @Mapping(target = "id", source = "shipmentState.id"),
            @Mapping(target = "shipmentState", source = "shipmentState.stateName"),
    })
    ShipmentStateResponse convertToShipmentStateResponse(ShipmentState shipmentState);
    AddressResponse convertFromAddressToAddressResponse(Address address);
    DrivingCategoryResponse convertToDrivingCategoryResponse(DrivingCategory drivingCategory);
    Set<DrivingCategoryResponse> convertToDrivingCategoryResponse(Set<DrivingCategory> drivingCategories);

    @Mappings({
            @Mapping(target = "id", source = "officeEmployee.id"),
            @Mapping(target = "office.officeAddress", source = "addressResponse")
    })
    OfficeEmployeeResponse convertToOfficeEmployeeResponse(OfficeEmployee officeEmployee, AddressResponse addressResponse);

    OfficeEmployeeResponse convertToOfficeEmployeeResponse(OfficeEmployee officeEmployee);
    SupplierResponse convertToSupplierResponse(Supplier supplier);
    VehicleResponse convertToVehicleResponse(Vehicle vehicle);
    @Mappings({
            @Mapping(target = "suppliers", ignore = true),
    })
    Vehicle convertToVehicle(VehicleResponse vehicle);
    @Mappings({
            @Mapping(target = "suppliers", ignore = true),
    })
    Set<DrivingCategory> convertToDrivingCategory(Set<DrivingCategoryResponse> drivingCategory);
    RecipientResponse convertToRecipientResponse(Recipient recipient);
    @Mappings({
            @Mapping(target = "receivedShipments", ignore = true),
    })
    Recipient convertToRecipient(RecipientResponse recipient);
    SenderResponse convertToSenderResponse(Sender sender);
    @Mappings({
            @Mapping(target = "sentShipments", ignore = true),
    })
    Sender convertToSender(SenderResponse sender);
}
