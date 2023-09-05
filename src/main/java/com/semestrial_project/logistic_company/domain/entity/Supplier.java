package com.semestrial_project.logistic_company.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Supplier")
public class Supplier extends BaseEmployee {

    @ManyToMany
    @JoinTable(
            name = "supplier_driving_category",
            joinColumns = @JoinColumn(name = "supplier_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<DrivingCategory> drivingLicenseCategories;

    @ManyToOne
    @JoinColumn(name = "vehicle")
    private Vehicle vehicle;
}
