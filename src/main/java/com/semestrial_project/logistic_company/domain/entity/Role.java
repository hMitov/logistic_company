package com.semestrial_project.logistic_company.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "role")
public class Role {

    public final static Long ADMIN = 1L;
    public final static Long CUSTOMER = 2L;
    public final static Long OFFICE_EMPLOYEE = 3L;
    public final static Long SUPPLIER = 4L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
