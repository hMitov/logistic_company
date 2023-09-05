package com.semestrial_project.logistic_company.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEmployee implements Comparable<BaseEmployee> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "egn", nullable = false)
    private String egn;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "salary", nullable = false)
    private Double salary;

    @Column(name = "date_of_employ", nullable = false)
    private LocalDate dateOfEmploy;

    @Column(name = "active", nullable = false)
    private boolean currentEmployee;

    @Override
    public int compareTo(BaseEmployee baseEmployee) {
        return this.egn.compareTo(baseEmployee.getEgn());
    }
}