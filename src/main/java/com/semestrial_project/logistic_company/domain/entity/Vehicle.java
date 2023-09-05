package com.semestrial_project.logistic_company.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "reg_plate_number", nullable = false)
    private String regPlateNumber;

    @Column(name = "vehicle_id", unique = true, nullable = false)
    private Long vehicleId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle")
    private Set<Supplier> suppliers;
}
