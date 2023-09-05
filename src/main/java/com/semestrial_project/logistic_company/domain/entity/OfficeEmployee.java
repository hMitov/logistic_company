package com.semestrial_project.logistic_company.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Office_Employee")
public class OfficeEmployee extends BaseEmployee {

    @ManyToOne
    @JoinColumn(name = "office")
    private Office office;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "registrant")
    private Set<Shipment> registeredShipments;

}
