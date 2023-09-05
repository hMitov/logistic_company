package com.semestrial_project.logistic_company.domain.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "recipient")
public class Recipient extends BaseCustomer {

    @OneToMany(mappedBy = "recipient")
    private List<Shipment> receivedShipments;
}
