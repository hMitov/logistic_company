package com.semestrial_project.logistic_company.web.view.controllers;

import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.services.OfficeService;
import com.semestrial_project.logistic_company.domain.services.ShipmentService;
import com.semestrial_project.logistic_company.web.adapters.WebAdapter;
import com.semestrial_project.logistic_company.web.view.models.address.AddressViewRequest;
import com.semestrial_project.logistic_company.web.view.models.shipment.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Controller
@AllArgsConstructor
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    private final OfficeService officeService;

    private final WebAdapter webAdapter;


    @GetMapping("/transport-ready")
    public String getTransportReadyShipments(Model model) {
        List<ShipmentViewResponse> shipments = shipmentService.getTransportReadyShipments().stream()
                .map(webAdapter::convertToShipmentView).collect(Collectors.toList());
        model.addAttribute("shipments", shipments);

        return "/shipments/transport-ready-shipments";
    }

    @GetMapping("/create-shipment")
    public String showCreateShipmentForm(Model model) {
        model.addAttribute("shipment", new CreateShipmentView());
        model.addAttribute("offices", officeService.getAllOffices());

        return "/shipments/create-shipment";
    }

    @PostMapping("/create")
    public String createShipment(@Valid @ModelAttribute("shipment") CreateShipmentView createShipmentView,
                                 BindingResult bindingResult,
                                 Model model) throws Exception {

        if (bindingResult.hasErrors()) {
            model.addAttribute("offices", officeService.getAllOffices());
            model.addAttribute("selectedSenderType", createShipmentView.getSender().getCustomerType());
            model.addAttribute("selectedDeparturePoint", createShipmentView.getDeparturePoint());
            model.addAttribute("selectedRecipientType", createShipmentView.getRecipient().getCustomerType());
            model.addAttribute("selectedArrivalPoint", createShipmentView.getArrivalPoint());
            return "/shipments/create-shipment";
        }
        shipmentService.createShipment(webAdapter.convertToCreateShipment(createShipmentView));

        return "redirect:/reference/shipments";
    }

    @GetMapping("/shipment-process/{id}")
    public String showProcessShipmentForm(Model model, @PathVariable Long id) throws Exception {
        model.addAttribute("processShipment", new ProcessShipmentView());
        processShipmentModel(id, model);

        return "/shipments/shipment-process";
    }

    @PostMapping("/process/{id}")
    public String processShipment(@PathVariable Long id,
                                  @Valid @ModelAttribute("processShipment") ProcessShipmentView processShipmentView,
                                  BindingResult bindingResult,
                                  Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            processShipmentModel(id, model);
            return "/shipments/shipment-process";
        }

        shipmentService.processShipment(id, webAdapter.convertToProcessShipment(processShipmentView));

        return "redirect:/reference/shipments";
    }

    private void processShipmentModel(Long id, Model model) throws DomainException {
        ShipmentViewResponse shipment = webAdapter.convertToShipmentView(shipmentService.getShipmentById(id));
        model.addAttribute("shipment", shipment);
    }

    @GetMapping(path = "/shipments/{id}")
    public ShipmentViewResponse getShipment(@PathVariable Long id) throws Exception {
        return webAdapter.convertToShipmentView(shipmentService.getShipmentById(id));
    }

    @GetMapping("/shipment-change-status/{id}")
    public String showChangeStatusShipmentForm(Model model, @PathVariable Long id) throws Exception {
        model.addAttribute("changeShipmentStatus", new ChangeStatusShipmentView());
        changeShipmentStatusModel(model, id);

        return "/shipments/shipment-change-status";
    }

    @PostMapping("/change-status/{id}")
    public String changeShipmentState(@PathVariable Long id,
                                      @Valid @ModelAttribute("changeShipmentStatus") ChangeStatusShipmentView changeStatusShipmentView,
                                      BindingResult bindingResult,
                                      Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            changeShipmentStatusModel(model, id);
            return "/shipments/shipment-change-status";
        }
        shipmentService.changeShipmentTransportState(id, webAdapter.convertToChangeStatusShipment(changeStatusShipmentView));

        return "redirect:/reference/shipments";
    }

    private void changeShipmentStatusModel(Model model, Long id) throws DomainException {
        ShipmentViewResponse shipment = webAdapter.convertToShipmentView(shipmentService.getShipmentById(id));
        List<ShipmentStateViewResponse> shipmentStateViewResponses = shipmentService.getAllShipmentStates()
                .stream().map(webAdapter::convertToShipmentStateViewResponse).collect(Collectors.toList());
        model.addAttribute("shipmentStates", shipmentStateViewResponses);
        model.addAttribute("shipment", shipment);
    }

    @GetMapping("/in-transport")
    public String getAllShipmentsInTransport(Model model) {
        List<ShipmentViewResponse> shipments = shipmentService.getAllShipmentsInTransport().stream()
                .map(webAdapter::convertToShipmentView).collect(Collectors.toList());
        model.addAttribute("shipments", shipments);

        return "/shipments/shipments-in-transport";
    }

    @GetMapping("/process")
    public String getAllShipmentsForProcessing(Model model) {
        List<ShipmentViewResponse> shipments = shipmentService.getAllShipmentsForProcessing().stream()
                .map(webAdapter::convertToShipmentView).collect(Collectors.toList());
        model.addAttribute("shipments", shipments);

        return "/shipments/shipments-for-processing";
    }

    @GetMapping("/{id}/remove")
    public String removeShipment(@PathVariable Long id) throws Exception {
        shipmentService.removeShipment(id);
        return "redirect:/reference/shipments";
    }
}
