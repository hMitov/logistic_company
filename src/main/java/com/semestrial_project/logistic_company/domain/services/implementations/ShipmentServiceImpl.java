package com.semestrial_project.logistic_company.domain.services.implementations;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.address.AddressResponse;
import com.semestrial_project.logistic_company.domain.dto.recipient.RecipientRs;
import com.semestrial_project.logistic_company.domain.dto.sender.SenderRs;
import com.semestrial_project.logistic_company.domain.dto.shipment.*;
import com.semestrial_project.logistic_company.domain.entity.*;
import com.semestrial_project.logistic_company.domain.entity.enums.SHIPMENT_POINT;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode;
import com.semestrial_project.logistic_company.domain.repository.*;
import com.semestrial_project.logistic_company.domain.services.OfficeService;
import com.semestrial_project.logistic_company.domain.services.RecipientService;
import com.semestrial_project.logistic_company.domain.services.SenderService;
import com.semestrial_project.logistic_company.domain.services.ShipmentService;
import com.semestrial_project.logistic_company.domain.services.validator.DomainValidator;
import com.semestrial_project.logistic_company.domain.utils.ShipmentStateUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode.*;
import static com.semestrial_project.logistic_company.domain.repository.specification.AddressSpecification.withCityStreetAndStreetNum;
import static com.semestrial_project.logistic_company.domain.repository.specification.ShipmentSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@AllArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    private final AddressRepository addressRepository;

    private final DomainAdapter domainAdapter;

    private final OfficeEmployeeRepository officeEmployeeRepository;

    private final ShipmentStateRepository shipmentStateRepository;

    private final ShipmentStateUtils shipmentStateUtils;

    private final SenderService senderService;

    private final RecipientService recipientService;

    private final OfficeService officeService;

    private final DomainValidator validator;

    @Transactional(rollbackFor = DomainException.class)
    public ShipmentResponse createShipment(CreateShipment shipmentData) throws DomainException {
        validator.validateShipmentOnCreate(shipmentData);

        if(shipmentData.getSender().getTelephone().equals(shipmentData.getRecipient().getTelephone())) {
            throw new DomainException(SHIPMENT_SENDER_SAME_AS_RECIPIENT__ERROR);
        }

        Address departureAddress;
        Address arrivalAddress;
        SHIPMENT_POINT departurePoint = SHIPMENT_POINT.valueOf(shipmentData.getDeparturePoint().toUpperCase());
        SHIPMENT_POINT arrivalPoint = SHIPMENT_POINT.valueOf(shipmentData.getArrivalPoint().toUpperCase());

        SenderRs senderRs = senderService.processSenderData(shipmentData.getSender(), shipmentData.getDeparturePoint());
        RecipientRs recipientRs = recipientService.processRecipientData(shipmentData.getRecipient(), shipmentData.getArrivalPoint());

        if (departurePoint.equals(SHIPMENT_POINT.OFFICE)) {
            departureAddress = extractOfficeAddress(shipmentData.getDepartureAddress());
        } else {
            departureAddress = getAddressById(senderRs.getAddress().getId());
        }

        if (arrivalPoint.equals(SHIPMENT_POINT.OFFICE)) {
            arrivalAddress = extractOfficeAddress(shipmentData.getArrivalAddress());
        } else {
            arrivalAddress = getAddressById(recipientRs.getAddress().getId());
        }

        OfficeEmployee registrant = null;
        if (!ObjectUtils.isEmpty(shipmentData.getRegistrantEmployeeId())) {
            registrant = getRegistrant(shipmentData.getRegistrantEmployeeId());
        }

        String externalId = UUID.randomUUID().toString();

        ShipmentState shipmentState = getShipmentState(SHIPMENT_POINT.OFFICE.toString().equalsIgnoreCase(shipmentData.getDeparturePoint()) ?
                ShipmentState.getState("CREATED") : ShipmentState.getState("READY_TO_TAKE_FROM_ADDRESS"));

        Sender sender = domainAdapter.convertToSender(senderService.getSenderById(senderRs.getId()));
        Recipient recipient = domainAdapter.convertToRecipient(recipientService.getRecipientById(recipientRs.getId()));

        Shipment shipment = Shipment.builder()
                .departureAddress(departureAddress)
                .arrivalAddress(arrivalAddress)
                .externalId(externalId)
                .departurePoint(departurePoint)
                .arrivalPoint(arrivalPoint)
                .shipmentState(shipmentState)
                .weight(null)
                .sender(sender)
                .recipient(recipient)
                .registrant(registrant)
                .deliveredDateTime(null)
                .build();

        return domainAdapter.convertToShipmentResponse(this.shipmentRepository.save(shipment));
    }

    public ShipmentResponse updateShipment(Long id, UpdateShipment shipmentData) throws DomainException {
        Shipment shipment = getShipment(id);
        validator.validateShipmentOnUpdate(shipmentData, shipment);

        if (!shipment.getShipmentState().getStateName().equals("CREATED")) {
            throw new DomainException(SHIPMENT_STATE_ERROR);
        }
        SHIPMENT_POINT departurePoint = Optional.ofNullable(shipmentData.getDeparturePoint()).isPresent() ?
                SHIPMENT_POINT.valueOf(shipmentData.getDeparturePoint()) : shipment.getDeparturePoint();

        SHIPMENT_POINT arrivalPoint = Optional.ofNullable(shipmentData.getArrivalPoint()).isPresent() ?
                SHIPMENT_POINT.valueOf(shipmentData.getArrivalPoint()) : shipment.getArrivalPoint();

        SenderRs senderRs = senderService.processSenderData(shipmentData.getSender(), shipmentData.getDeparturePoint());
        RecipientRs recipientRs = recipientService.processRecipientData(shipmentData.getRecipient(), shipmentData.getArrivalPoint());
        Address departureAddress;
        Address arrivalAddress;
        if (Optional.ofNullable(shipmentData.getDepartureAddress()).isPresent()) {
            if (departurePoint.equals(SHIPMENT_POINT.OFFICE)) {
                departureAddress = extractOfficeAddress(shipmentData.getDepartureAddress());
            } else {
                departureAddress = getAddressById(senderRs.getAddress().getId());
            }
        } else {
            departureAddress = shipment.getDepartureAddress();
        }

        if (Optional.ofNullable(shipmentData.getArrivalAddress()).isPresent()) {
            if (arrivalPoint.equals(SHIPMENT_POINT.OFFICE)) {
                arrivalAddress = extractOfficeAddress(shipmentData.getArrivalAddress());
            } else {
                arrivalAddress = getAddressById(recipientRs.getAddress().getId());
            }
        } else {
            arrivalAddress = shipment.getArrivalAddress();
        }

        OfficeEmployee registrant;
        if (Optional.ofNullable(shipmentData.getRegistrantEmployeeId()).isPresent()) {
            registrant = getRegistrant(shipmentData.getRegistrantEmployeeId());
        } else {
            registrant = shipment.getRegistrant();
        }

        ShipmentState shipmentState;
        if (Optional.ofNullable(shipmentData.getShipmentState()).isPresent()) {
            String state = shipmentStateUtils.extractShipmentState(shipmentData.getShipmentState());
            shipmentState = getShipmentState(ShipmentState.getState(state));
        } else {
            shipmentState = shipment.getShipmentState();
        }

        Sender sender = domainAdapter.convertToSender(senderService.getSenderById(senderRs.getId()));
        Recipient recipient = domainAdapter.convertToRecipient(recipientService.getRecipientById(recipientRs.getId()));

        shipment = shipment.toBuilder()
                .departureAddress(departureAddress)
                .arrivalAddress(arrivalAddress)
                .departurePoint(departurePoint)
                .arrivalPoint(arrivalPoint)
                .shipmentState(shipmentState)
                .sender(sender)
                .recipient(recipient)
                .registrant(registrant)
                .build();

        return domainAdapter.convertToShipmentResponse(this.shipmentRepository.save(shipment));
    }

    private Address getAddressById(Long id) throws DomainException {
        return addressRepository.findById(id)
                .orElseThrow(() -> new DomainException(ADDRESS_NOT_FOUND));
    }

    private OfficeEmployee getRegistrant(Long id) throws DomainException {
        return officeEmployeeRepository.findByEmployeeId(id)
                .orElseThrow(() -> new DomainException(REGISTRANT_NOT_FOUND));
    }

    private Shipment getShipment(Long id) throws DomainException {
        return this.shipmentRepository.findById(id)
                .orElseThrow(() -> new DomainException(SHIPMENT_NOT_FOUND));
    }

    private Address extractOfficeAddress(String address) throws DomainException {
        Office office = officeService.extractOfficeData(address);
        AddressResponse addressResponse = officeService.getOfficeById(office.getId()).getOfficeAddress();
        List<Address> addresses = addressRepository.findAll(where(withCityStreetAndStreetNum(
                addressResponse.getCity(),
                addressResponse.getStreet(),
                addressResponse.getStreetNum())
        ));
        if (addresses.isEmpty()) {
            throw new DomainException(OFFICE_ADDRESS_INVALID);
        }

        return addresses.get(0);
    }

    public ShipmentResponse getShipmentById(Long id) throws DomainException {
        Shipment shipmentLoaded = this.shipmentRepository.findById(id)
                .orElseThrow(() -> new DomainException(SHIPMENT_NOT_FOUND));
        return domainAdapter.convertToShipmentResponse(shipmentLoaded);
    }

    public List<ShipmentResponse> getAllShipments() {
        return this.shipmentRepository.findAll().stream().map(domainAdapter::convertToShipmentResponse)
                .collect(Collectors.toList());
    }

    public List<ShipmentResponse> getTransportReadyShipments() {
        return this.shipmentRepository.findAll(where(withReadyForTransportStatus())).stream().map(domainAdapter::convertToShipmentResponse)
                .collect(Collectors.toList());
    }

    public void processShipment(Long id, ProcessShipment processShipment) throws DomainException {
        OfficeEmployee officeEmployee = officeEmployeeRepository.findByEmployeeId(processShipment.getProcessedByEmployeeId())
                .orElseThrow(() -> new DomainException(OFFICE_EMPLOYEE_NOT_FOUND));
        Shipment shipment = shipmentRepository.findById(id).orElseThrow(() -> new DomainException(SHIPMENT_NOT_FOUND));

        double price = calculateShipmentPrice(processShipment.getWeight(), shipment.getDeparturePoint(), shipment.getArrivalPoint());

        ShipmentState shipmentState = getShipmentState(ShipmentState.getState("PROCESSED_AND_READY_FOR_DELIVERY"));

        if (shipment.getShipmentState().equals(shipmentState)) {
            throw new DomainException(SHIPMENT_ALREADY_PROCESSED);
        }
        shipment = shipment.toBuilder()
                .processedByEmployee(officeEmployee)
                .shipmentState(shipmentState)
                .weight(processShipment.getWeight())
                .price(price)
                .build();
        shipmentRepository.save(shipment);
    }

    private double calculateShipmentPrice(double weight, SHIPMENT_POINT departurePoint, SHIPMENT_POINT arrivalPoint) {
        double standardWeightPrice = 2.0;
        double fromAddressPrice = 4.0;
        double toAddressPrice = 4.0;
        double finalPrice = 0;

        finalPrice += standardWeightPrice;

        if (weight > 50.0) {
            finalPrice += standardWeightPrice;
        } else if (weight > 30.0) {
            finalPrice += 0.8 * standardWeightPrice;
        } else if (weight > 10.0) {
            finalPrice += 0.5 * standardWeightPrice;
        } else if (weight > 5.0) {
            finalPrice += 0.3 * standardWeightPrice;
        }

        if (departurePoint.equals(SHIPMENT_POINT.ADDRESS)) {
            finalPrice += fromAddressPrice;
        }
        if (arrivalPoint.equals(SHIPMENT_POINT.ADDRESS)) {
            finalPrice += toAddressPrice;
        }

        return finalPrice;
    }

    private ShipmentState getShipmentState(Long id) throws DomainException {
        return shipmentStateRepository.findById(id).orElseThrow(() -> new DomainException(SHIPMENT_STATE_INVALID));
    }

    public void changeShipmentTransportState(Long id, ChangeStatusShipment changeStatusShipment) throws DomainException {
        Shipment shipmentLoaded = this.shipmentRepository.findById(id).orElseThrow(() -> new DomainException(SHIPMENT_NOT_FOUND));
        if (shipmentLoaded.getShipmentState().getId().equals(ShipmentState.getState("ARRIVED_TO_RECIPIENT"))) {
            throw new DomainException(SHIPMENT_STATE_ALREADY_MOVED_TO_ARRIVED);
        }
        ShipmentState shipmentState = getShipmentState(ShipmentState.getState(
                shipmentStateUtils.extractShipmentState(changeStatusShipment.getShipmentStatus())));

        shipmentLoaded = shipmentLoaded.toBuilder()
                .shipmentState(shipmentState).build();

        if (shipmentState.getId().equals(ShipmentState.getState("ARRIVED_TO_RECIPIENT"))) {
            shipmentLoaded = shipmentLoaded.toBuilder()
                    .deliveredDateTime(LocalDateTime.now()).build();
        }

        shipmentRepository.save(shipmentLoaded);
    }

    public List<ShipmentStateResponse> getAllShipmentStates() {
        return shipmentStateRepository.findAll().stream().map(domainAdapter::convertToShipmentStateResponse)
                .collect(Collectors.toList());
    }

    public List<ShipmentResponse> getAllShipmentsInTransport() {
        return this.shipmentRepository.findAll(where(withInTransportStatus())).stream().map(domainAdapter::convertToShipmentResponse)
                .collect(Collectors.toList());
    }

    public List<ShipmentResponse> getAllShipmentsInTransportToRecipient() {
        return this.shipmentRepository.findAll(where(withTravelToRecipientStatus())).stream().map(domainAdapter::convertToShipmentResponse)
                .collect(Collectors.toList());
    }

    public List<ShipmentResponse> getAllShipmentsByRegistrantEmployeeId(Long employeeId) {
        return this.shipmentRepository.findAll(where(withRegistrantEmployeeId(employeeId))).stream().map(domainAdapter::convertToShipmentResponse)
                .collect(Collectors.toList());
    }

    public List<ShipmentResponse> getAllShipmentsSentBySender(String telephone) {
        return this.shipmentRepository.findAll(where(withSenderTelephone(telephone))).stream().map(domainAdapter::convertToShipmentResponse)
                .collect(Collectors.toList());
    }

    public List<ShipmentResponse> getAllShipmentsReceivedByRecipient(String telephone) {
        return this.shipmentRepository.findAll(where(withRecipientTelephoneAndReceivedStatus(telephone))).stream().map(domainAdapter::convertToShipmentResponse)
                .collect(Collectors.toList());
    }

    public double getIncomeFromShipments(CompanyIncome companyIncome) throws DomainException {
        validator.validateIncomeFromShipments(companyIncome);

        LocalDateTime start = companyIncome.getStartDate().atStartOfDay();
        LocalDateTime end = companyIncome.getEndDate().atStartOfDay();
        return this.shipmentRepository.findAll(where(withShipmentBetweenPeriod(start, end))).stream().map(Shipment::getPrice)
                .filter(Objects::nonNull).mapToDouble(Double::doubleValue).sum();
    }

    public List<ShipmentResponse> getAllShipmentsForProcessing() {
        return this.shipmentRepository.findAll(where(withArrivedInOfficeOrCreatedStatus())).stream().map(domainAdapter::convertToShipmentResponse)
                .collect(Collectors.toList());
    }

    public void removeShipment(Long id) throws DomainException {
        getShipment(id);
        shipmentRepository.deleteById(id);
    }
}