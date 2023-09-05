package com.semestrial_project.logistic_company.web.view.controllers;

import com.semestrial_project.logistic_company.domain.services.VehicleService;
import com.semestrial_project.logistic_company.web.adapters.WebAdapter;
import com.semestrial_project.logistic_company.web.view.models.vehicle.CreateVehicleView;
import com.semestrial_project.logistic_company.web.view.models.vehicle.UpdateVehicleView;
import com.semestrial_project.logistic_company.web.view.models.vehicle.VehicleViewResponse;
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
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    private final WebAdapter webAdapter;

    @GetMapping
    public String getAllVehicles(Model model) {
        List<VehicleViewResponse> vehicles = vehicleService.getAllVehicles().stream()
                .map(webAdapter::convertToVehicleViewResponse).collect(Collectors.toList());
        model.addAttribute("vehicles", vehicles);

        return "/vehicles/vehicles.html";
    }

    @GetMapping("/create-vehicle")
    public String showCreateVehicleForm(Model model) {
        model.addAttribute("vehicle", new CreateVehicleView());

        return "/vehicles/create-vehicle";
    }

    @PostMapping("/create")
    public String createVehicle(@Valid @ModelAttribute("vehicle") CreateVehicleView createVehicleView,
                                BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "/vehicles/create-vehicle";
        }
        vehicleService.createVehicle(webAdapter.convertToCreateVehicle(createVehicleView));

        return "redirect:/vehicles";
    }

    @RequestMapping(path = "/vehicles/{id}", method = RequestMethod.GET)
    public VehicleViewResponse getVehicle(@PathVariable Long id) throws Exception {
        return webAdapter.convertToVehicleViewResponse(vehicleService.getVehicleById(id));
    }

    @GetMapping("/edit-vehicle/{id}")
    public String showEditVehicleForm(Model model, @PathVariable Long id) throws Exception {
        VehicleViewResponse vehicle = webAdapter
                .convertToVehicleViewResponse(vehicleService.getVehicleById(id));
        model.addAttribute("vehicle", vehicle);

        return "/vehicles/edit-vehicle";
    }

    @PatchMapping("/update/{id}")
    public String updateVehicle(@PathVariable Long id,
                                @ModelAttribute("vehicle") UpdateVehicleView updateVehicleView) throws Exception {
        vehicleService.updateVehicle(id, webAdapter.convertToUpdateVehicle(updateVehicleView));

        return "redirect:/vehicles";
    }

    @GetMapping("/{id}/remove")
    public String removeVehicle(@PathVariable Long id) throws Exception {
        vehicleService.removeVehicle(id);
        return "redirect:/vehicles";
    }
}
