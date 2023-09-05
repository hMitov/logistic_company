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
@Table(name = "sender")
public class Sender extends BaseCustomer {

    @OneToMany(mappedBy = "sender")
    private List<Shipment> sentShipments;
}
