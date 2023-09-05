package com.semestrial_project.logistic_company.domain.entity;

import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode.SHIPMENT_STATE_ERROR;
import static com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode.SHIPMENT_STATE_INVALID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipment_state")
public class ShipmentState {

    private static final Long READY_TO_TAKE_FROM_ADDRESS = 1L;
    private static final Long TRAVEL_TO_OFFICE = 2L;
    private static final Long ARRIVED_IN_OFFICE = 3L;
    private static final Long CREATED = 4L;
    private static final Long PROCESSED_AND_READY_FOR_DELIVERY = 5L;
    private static final Long TRAVEL_TO_RECIPIENT = 6L;
    private static final Long ARRIVED_TO_RECIPIENT = 7L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state_name")
    private String stateName;

    @OneToMany(mappedBy = "shipmentState", fetch = FetchType.LAZY)
    private List<Shipment> shipments;

    public static Long getState(String state) throws DomainException {
        Long result;
        switch (state) {
            case "CREATED":
                result = CREATED;
                break;
            case "PROCESSED_AND_READY_FOR_DELIVERY":
                result = PROCESSED_AND_READY_FOR_DELIVERY;
                break;
            case "TRAVEL_TO_OFFICE":
                result = TRAVEL_TO_OFFICE;
                break;
            case "ARRIVED_IN_OFFICE":
                result = ARRIVED_IN_OFFICE;
                break;
            case "TRAVEL_TO_RECIPIENT":
                result = TRAVEL_TO_RECIPIENT;
                break;
            case "ARRIVED_TO_RECIPIENT":
                result = ARRIVED_TO_RECIPIENT;
                break;
            case "READY_TO_TAKE_FROM_ADDRESS":
                result = READY_TO_TAKE_FROM_ADDRESS;
                break;
            default:
                throw new DomainException(SHIPMENT_STATE_INVALID);
        }

        return result;
    }

}
