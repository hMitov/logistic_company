package com.semestrial_project.logistic_company.domain.repository.specification;

import com.semestrial_project.logistic_company.domain.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Join;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ShipmentSpecification {
    public static Specification<Shipment> withReadyForTransportStatus() {
        Long READY_TO_TAKE_FROM_ADDRESS = 1L;
        Long PROCESSED_AND_READY_FOR_DELIVERY = 5L;
        return (shipment, cq, cb) -> {
            Join<Shipment, ShipmentState> shipmentStateJoin = shipment.join("shipmentState");
            return cb.or(
                    cb.equal(shipmentStateJoin.get("id"), READY_TO_TAKE_FROM_ADDRESS),
                    cb.equal(shipmentStateJoin.get("id"), PROCESSED_AND_READY_FOR_DELIVERY)
            );
        };
    }

    public static Specification<Shipment> withInTransportStatus() {
        Long TRAVEL_TO_OFFICE = 2L;
        Long TRAVEL_TO_RECIPIENT = 6L;
        return (shipment, cq, cb) -> {
            Join<Shipment, ShipmentState> shipmentStateJoin = shipment.join("shipmentState");
            return cb.or(
                    cb.equal(shipmentStateJoin.get("id"), TRAVEL_TO_OFFICE),
                    cb.equal(shipmentStateJoin.get("id"), TRAVEL_TO_RECIPIENT)
            );
        };
    }

    public static Specification<Shipment> withTravelToRecipientStatus() {
        Long TRAVEL_TO_RECIPIENT = 6L;
        return (shipment, cq, cb) -> {
            Join<Shipment, ShipmentState> shipmentStateJoin = shipment.join("shipmentState");
            return cb.equal(shipmentStateJoin.get("id"), TRAVEL_TO_RECIPIENT);
        };
    }

    public static Specification<Shipment> withArrivedInOfficeOrCreatedStatus() {
        Long ARRIVED_IN_OFFICE = 3L;
        Long CREATED = 4L;
        return (shipment, cq, cb) -> {
            Join<Shipment, ShipmentState> shipmentStateJoin = shipment.join("shipmentState");
            return cb.or(
                    cb.equal(shipmentStateJoin.get("id"), ARRIVED_IN_OFFICE),
                    cb.equal(shipmentStateJoin.get("id"), CREATED)
            );
        };
    }

    public static Specification<Shipment> withRegistrantEmployeeId(Long employeeId) {
        if (ObjectUtils.isEmpty(employeeId)) {
            return (shipment, cq, cb) -> cb.conjunction();
        }
        return (shipment, cq, cb) -> {
            Join<Shipment, OfficeEmployee> shipmentRegistrantJoin = shipment.join("registrant");
            return cb.equal(shipmentRegistrantJoin.get("employeeId"), employeeId);

        };
    }

    public static Specification<Shipment> withSenderTelephone(String telephone) {
        if (ObjectUtils.isEmpty(telephone)) {
            return (shipment, cq, cb) -> cb.conjunction();
        }
        return (shipment, cq, cb) -> {
            Join<Shipment, Sender> shipmentSenderJoin = shipment.join("sender");
            return cb.equal(shipmentSenderJoin.get("telephone"), telephone);
        };
    }

    public static Specification<Shipment> withRecipientTelephoneAndReceivedStatus(String telephone) {
        Long ARRIVED_TO_RECIPIENT = 7L;
        if (ObjectUtils.isEmpty(telephone)) {
            return (shipment, cq, cb) -> cb.conjunction();
        }
        return (shipment, cq, cb) -> {
            Join<Shipment, Recipient> shipmentRecipientJoin = shipment.join("recipient");
            Join<Shipment, ShipmentState> shipmentStateJoin = shipment.join("shipmentState");
            return cb.and(
                    cb.equal(shipmentRecipientJoin.get("telephone"), telephone),
                    cb.equal(shipmentStateJoin.get("id"), ARRIVED_TO_RECIPIENT)
            );
        };
    }

    public static Specification<Shipment> withShipmentBetweenPeriod(LocalDateTime start, LocalDateTime end) {
        if (ObjectUtils.isEmpty(start) || ObjectUtils.isEmpty(end)) {
            return (shipment, cq, cb) -> cb.conjunction();
        }
        return (shipment, cq, cb) -> cb.between(shipment.get("deliveredDateTime"), start, end);
    }
}
