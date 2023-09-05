package com.semestrial_project.logistic_company.domain.repository;

import com.semestrial_project.logistic_company.domain.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long>, JpaSpecificationExecutor<Shipment> {

    Optional<Shipment> findShipmentsByExternalId(String externalId);
//
//    List<Shipment> findShipmentsByRegistrant(OfficeEmployee registrant);
//
//    List<Shipment> findAllBy();
//
//    List<Shipment> findShipmentsBySender(Customer customer);
//
//    <T> Optional<Address> findAll(Specification<T> where);

//    @Query("SELECT s FROM Shipment s WHERE s.isProcessed = true AND s.isDelivered = false")
//    List<Shipment> findAllProcessedAndNotDelivered();
//
//    List<Shipment> findShipmentsByRecipient(Recipient recipient);
//
//    @Query("SELECT s FROM Shipment s WHERE s.isDelivered = true")
//    List<Shipment> findAllDelivered();
//
//    List<Shipment> findAllByCity(String city);
//
//    List<Shipment> findAllByWeightGreaterThanEqual(Double weight);
//
//    @Query("SELECT s FROM Shipment s WHERE s.toOffice= true")
//    List<Shipment> findAllToOffice();
//
//    List<Shipment> findAllByToOfficeIsFalse();
//
//    List<Shipment> findAllByToAddressIsFalse();
//
//    List<Shipment> findAllByCityAndToAddress(String city, boolean toAddress);
//
//    List<Shipment> findShipmentsByRecipientId(Long id);
}