package com.semestrial_project.logistic_company.domain.repository.specification;

import com.semestrial_project.logistic_company.domain.entity.Vehicle;
import org.springframework.data.jpa.domain.Specification;

public class VehicleSpecification {

    public static Specification<Vehicle> withRegPlateNum(String regPlateNumber) {
        return (vehicle, cq, cb) ->
                cb.equal(vehicle.get("regPlateNumber"), regPlateNumber);
    }
}
