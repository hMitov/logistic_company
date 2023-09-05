package com.semestrial_project.logistic_company.domain.repository.specification;

import com.semestrial_project.logistic_company.domain.entity.Address;
import com.semestrial_project.logistic_company.domain.entity.Office;
import com.semestrial_project.logistic_company.domain.entity.Recipient;
import com.semestrial_project.logistic_company.domain.entity.Sender;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Join;

public class AddressSpecification {

    public static Specification<Address> withCityStreetAndStreetNum(String city, String street, String streetNum) {
        if (ObjectUtils.isEmpty(city) || ObjectUtils.isEmpty(street) || ObjectUtils.isEmpty(streetNum)) {
            return (address, cq, cb) -> cb.conjunction();
        }
        return (address, cq, cb) ->
                cb.and(
                        cb.equal(address.get("city"), city),
                        cb.equal(address.get("street"), street),
                        cb.equal(address.get("streetNum"), streetNum)
                );
    }

//    public static Specification<Address> withCityStreetStreetNumAndActiveOffice(String city, String street, String streetNum) {
//        if (ObjectUtils.isEmpty(city) || ObjectUtils.isEmpty(street) || ObjectUtils.isEmpty(streetNum)) {
//            return (address, cq, cb) -> cb.conjunction();
//        }
//        return (address, cq, cb) ->
//                cb.and(
//                        cb.equal(address.get("city"), city),
//                        cb.equal(address.get("street"), street),
//                        cb.equal(address.get("streetNum"), streetNum),
//                        cb.equal(address.get("activeOffice"), true)
//                );
//    }

    public static Specification<Address> withSenderIdAndActiveSender(Long senderId) {
        if (ObjectUtils.isEmpty(senderId)) {
            return (address, cq, cb) -> cb.conjunction();
        } else {
            return (address, cq, cb) -> {
                Join<Sender, Address> senderJoin = address.join("sender");
                return cb.and(
                        cb.equal(senderJoin.get("id"), senderId),
                        cb.equal(address.get("activeSender"), true)
                );
            };
        }
    }

    public static Specification<Address> withOfficeIdAndActiveOffice(Long officeId) {
        if (ObjectUtils.isEmpty(officeId)) {
            return (address, cq, cb) -> cb.conjunction();
        } else {
            return (address, cq, cb) -> {
                Join<Office, Address> officeJoin = address.join("office");
                return cb.and(
                        cb.equal(officeJoin.get("id"), officeId),
                        cb.equal(address.get("activeOffice"), true)
                );
            };
        }
    }

    public static Specification<Address> withRecipientIdAndActiveRecipient(Long recipientId) {
        if (ObjectUtils.isEmpty(recipientId)) {
            return (address, cq, cb) -> cb.conjunction();
        } else {
            return (address, cq, cb) -> {
                Join<Recipient, Address> recipientJoin = address.join("recipient");
                return cb.and(
                        cb.equal(recipientJoin.get("id"), recipientId),
                        cb.equal(address.get("activeRecipient"), true)
                );
            };
        }
    }
}
