package com.semestrial_project.logistic_company.domain.entity;

import com.semestrial_project.logistic_company.domain.entity.enums.CUSTOMER_TYPE;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class BaseCustomer implements Comparable<BaseCustomer>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "first_name")
    protected String firstName;

    @Column(name = "last_name")
    protected String lastName;

    @Column(name = "organization_name")
    protected String organizationName;

    @Enumerated(EnumType.STRING)
    protected CUSTOMER_TYPE customerType;

    @OneToMany(mappedBy = "recipient")
    protected List<Address> addresses;

    @Column(name = "telephone", nullable = false)
    protected String telephone;

    @Column(name = "email", nullable = false)
    protected String email;

    @Column(name = "special_instructions")
    private String specialInstructions;

    @Override
    public int compareTo(BaseCustomer baseCustomer) {
        return this.telephone.compareTo(baseCustomer.getTelephone());
    }
}
