package com.semestrial_project.logistic_company.domain.utils;

import com.semestrial_project.logistic_company.domain.entity.ShipmentState;
import com.semestrial_project.logistic_company.domain.repository.ShipmentStateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@AllArgsConstructor
public class ShipmentStateUtils {

    private final ShipmentStateRepository shipmentStateRepository;

    public String extractShipmentState(String shipmentStateName) {
        return shipmentStateName.replace(' ', '_').toUpperCase(Locale.ROOT);
    }
}
