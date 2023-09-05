package com.semestrial_project.logistic_company.domain.entity;

import com.semestrial_project.logistic_company.domain.entity.enums.SHIPMENT_POINT;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "shipment")
public class Shipment implements Comparable<Shipment> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "arrival_address", nullable = false)
    private Address arrivalAddress;

    @ManyToOne
    @JoinColumn(name = "departure_address", nullable = false)
    private Address departureAddress;

    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Enumerated(EnumType.STRING)
    private SHIPMENT_POINT departurePoint;

    @Enumerated(EnumType.STRING)
    private SHIPMENT_POINT arrivalPoint;

    @ManyToOne
    @JoinColumn(name = "shipment_state", nullable = false)
    private ShipmentState shipmentState;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "price")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private Sender sender;

    @ManyToOne
    @JoinColumn(name = "recipient", nullable = false)
    private Recipient recipient;

    @ManyToOne
    @JoinColumn(name = "registered_by_employee")
    private OfficeEmployee registrant;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private OfficeEmployee processedByEmployee;

    @Column(name = "delivered_date_time")
    private LocalDateTime deliveredDateTime;

    @Override
    public int compareTo(Shipment shipment) {
        return Long.compare(this.id, shipment.id);
    }

}
