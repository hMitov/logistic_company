package com.semestrial_project.logistic_company.domain.repository.specification;

import com.semestrial_project.logistic_company.domain.entity.Address;
import com.semestrial_project.logistic_company.domain.entity.Office;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Join;

public class OfficeSpecification {

    public static Specification<Office> withCityStreetStreetNum(String city, String street, String streetNum) {
        if (ObjectUtils.isEmpty(city) || ObjectUtils.isEmpty(street) || ObjectUtils.isEmpty(streetNum)) {
            return (address, cq, cb) -> cb.conjunction();
        }
        return (address, cq, cb) -> {
            Join<Address, Office> addresJoin = address.join("addresses");
            return cb.and(
                    cb.equal(addresJoin.get("city"), city),
                    cb.equal(addresJoin.get("street"), street),
                    cb.equal(addresJoin.get("streetNum"), streetNum),
                    cb.equal(addresJoin.get("activeOffice"), true)
            );
        };
    }
}
