package com.semestrial_project.logistic_company.web.view.controllers;

import com.semestrial_project.logistic_company.domain.dto.shipment.ShipmentsByEmployeeView;
import com.semestrial_project.logistic_company.domain.dto.shipment.ShipmentsByRecipientView;
import com.semestrial_project.logistic_company.domain.dto.shipment.ShipmentsBySenderView;
import com.semestrial_project.logistic_company.domain.services.OfficeEmployeeService;
import com.semestrial_project.logistic_company.domain.services.ShipmentService;
import com.semestrial_project.logistic_company.domain.services.SupplierService;
import com.semestrial_project.logistic_company.web.adapters.WebAdapter;
import com.semestrial_project.logistic_company.web.view.models.office_employee.OfficeEmployeeViewResponse;
import com.semestrial_project.logistic_company.web.view.models.shipment.CompanyIncomeView;
import com.semestrial_project.logistic_company.web.view.models.shipment.ShipmentViewResponse;
import com.semestrial_project.logistic_company.web.view.models.supplier.SupplierViewResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/reference")
public class ReferenceController {

    private final ShipmentService shipmentService;

    private final OfficeEmployeeService officeEmployeeService;

    private final SupplierService supplierService;

    private final WebAdapter webAdapter;


    @GetMapping("/shipments")
    public String getAllShipments(Model model) {
        List<ShipmentViewResponse> shipments = shipmentService.getAllShipments().stream()
                .map(webAdapter::convertToShipmentView).collect(Collectors.toList());
        model.addAttribute("shipments", shipments);

        return "/shipments/shipments";
    }

    @GetMapping("/shipments-by-employee")
    public String showShipmentsByEmployeeForm(Model model) {
        model.addAttribute("shipmentsByEmployee", new ShipmentsByEmployeeView());
        return "/references/shipments-by-employee";
    }

    @GetMapping("/shipments/registered-by-office-employee")
    public String getAllShipmentsByRegistrantEmployee(@Valid @ModelAttribute("shipmentsByEmployee") ShipmentsByEmployeeView shipmentsByEmployeeView,
                                                      BindingResult bindingResult,
                                                      Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return "/references/shipments-by-employee";
        }

        Long longValue = Long.parseLong(shipmentsByEmployeeView.getEmployeeId());
        List<ShipmentViewResponse> shipments = shipmentService.getAllShipmentsByRegistrantEmployeeId(longValue)
                .stream().map(webAdapter::convertToShipmentView).collect(Collectors.toList());
        model.addAttribute("shipments", shipments);

        return "/shipments/shipments";
    }

    @GetMapping("/shipments-by-sender")
    public String showShipmentsBySenderForm(Model model) {
        model.addAttribute("shipmentsBySender", new ShipmentsBySenderView());
        return "/references/shipments-by-sender";
    }

    @GetMapping("/shipments/sent-by-sender")
    public String getAllShipmentsSentBySender(@Valid
                                              @ModelAttribute("shipmentsBySender") ShipmentsBySenderView shipmentsBySenderView,
                                              BindingResult bindingResult,
                                              Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return "/references/shipments-by-sender";
        }

        List<ShipmentViewResponse> shipments = shipmentService.getAllShipmentsSentBySender(shipmentsBySenderView.getTelephone())
                .stream().map(webAdapter::convertToShipmentView).collect(Collectors.toList());
        model.addAttribute("shipments", shipments);

        return "/shipments/shipments";
    }

    @GetMapping("/shipments/sent")
    public String getAllShipmentsInTransportToRecipient(Model model) throws Exception {
        List<ShipmentViewResponse> shipments = shipmentService.getAllShipmentsInTransportToRecipient()
                .stream().map(webAdapter::convertToShipmentView).collect(Collectors.toList());
        model.addAttribute("shipments", shipments);

        return "/shipments/shipments";
    }

    @GetMapping("/shipments-by-recipient")
    public String showShipmentsByRecipientForm(Model model) {
        model.addAttribute("shipmentsByRecipient", new ShipmentsByRecipientView());
        return "/references/shipments-by-recipient";
    }

    @GetMapping("/shipments/received-by-recipient")
    public String getAllShipmentsReceivedByRecipient(@Valid
                                                     @ModelAttribute("shipmentsByRecipient") ShipmentsByRecipientView shipmentsByRecipientView,
                                                     BindingResult bindingResult,
                                                     Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return "/references/shipments-by-recipient";
        }

        List<ShipmentViewResponse> shipments = shipmentService.getAllShipmentsReceivedByRecipient(shipmentsByRecipientView.getTelephone())
                .stream().map(webAdapter::convertToShipmentView).collect(Collectors.toList());
        model.addAttribute("shipments", shipments);

        return "/shipments/shipments";
    }

    @GetMapping("/shipments/employees")
    public String getAllEmployees(Model model) throws Exception {
        List<OfficeEmployeeViewResponse> officeEmployees = officeEmployeeService.getAllOfficeEmployees()
                .stream().map(webAdapter::convertToOfficeEmployeeViewResponse).collect(Collectors.toList());

        List<SupplierViewResponse> suppliers = supplierService.getAllSuppliers().stream()
                .map(supplier -> webAdapter.convertToSupplierViewResponse(supplier, supplier.getDrivingLicenseCategories()))
                .collect(Collectors.toList());

        model.addAttribute("officeEmployees", officeEmployees);
        model.addAttribute("suppliers", suppliers);

        return "/references/employees";
    }

    @GetMapping("/income-for-period")
    public String showIncomeFormShipments(Model model) throws Exception {
        model.addAttribute("shipmentIncome", new CompanyIncomeView());

        return "/references/insert-income-period";
    }

    @GetMapping("/income")
    public String getIncomeFromShipments(@Valid @ModelAttribute("shipmentIncome") CompanyIncomeView companyIncomeView,
                                         BindingResult bindingResult,
                                         Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return "/references/insert-income-period";
        }
        double income = shipmentService.getIncomeFromShipments(webAdapter.convertToShipmentIncome(companyIncomeView));
        model.addAttribute("income", income);

        return "/references/total-income";
    }
}
