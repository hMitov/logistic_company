package com.semestrial_project.logistic_company.domain.repository;

import com.semestrial_project.logistic_company.domain.entity.OfficeEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfficeEmployeeRepository extends JpaRepository<OfficeEmployee, Long> {

    Optional<OfficeEmployee> findOfficeEmployeeByEgn(String egn);
    Optional<OfficeEmployee> findByEmployeeId(Long employeeId);
}
