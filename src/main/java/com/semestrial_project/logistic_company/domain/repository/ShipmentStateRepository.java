package com.semestrial_project.logistic_company.domain.repository;

import com.semestrial_project.logistic_company.domain.entity.ShipmentState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentStateRepository extends JpaRepository<ShipmentState, Long> {
}
