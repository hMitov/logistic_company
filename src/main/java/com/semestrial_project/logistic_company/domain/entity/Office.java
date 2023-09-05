package com.semestrial_project.logistic_company.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "office")
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_establishment", nullable = false)
    private LocalDate dateOfEstablishment;

    @Column(name = "telephone", nullable = false)
    private String telephone;

    @OneToMany(mappedBy = "office")
    private List<Address> addresses;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "office")
    private List<OfficeEmployee> officeEmployees;
}
