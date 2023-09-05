package com.semestrial_project.logistic_company.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "post_code", nullable = false)
    private String postCode;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "street_num", nullable = false)
    private String streetNum;

    @ManyToOne
    @JoinColumn(name = "recipient")
    private Recipient recipient;

    @ManyToOne
    @JoinColumn(name = "sender")
    private Sender sender;

    @ManyToOne
    @JoinColumn(name = "office")
    private Office office;

    @Column(name = "activeOffice", nullable = false)
    private boolean activeOffice;

    @Column(name = "activeSender", nullable = false)
    private boolean activeSender;

    @Column(name = "activeRecipient", nullable = false)
    private boolean activeRecipient;

    @OneToMany(mappedBy = "arrivalAddress", fetch = FetchType.LAZY)
    private List<Shipment> arrivalShipments;

    @OneToMany(mappedBy = "departureAddress", fetch = FetchType.LAZY)
    private List<Shipment> departureShipments;
}
