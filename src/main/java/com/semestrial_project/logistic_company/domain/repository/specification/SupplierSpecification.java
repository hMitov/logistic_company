package com.semestrial_project.logistic_company.domain.repository.specification;

import com.semestrial_project.logistic_company.domain.entity.Supplier;
import com.semestrial_project.logistic_company.domain.entity.Vehicle;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Join;

public class SupplierSpecification {

    public static Specification<Supplier> withVehicleId(Long vehicleId) {
        if (ObjectUtils.isEmpty(vehicleId)) {
            return (supplier, cq, cb) -> cb.conjunction();
        }
        return (supplier, cq, cb) -> {
            Join<Vehicle, Supplier> vehicleJoin = supplier.join("vehicle");
            return cb.and(
                    cb.equal(vehicleJoin.get("vehicleId"), vehicleId)
            );
        };
    }
}
