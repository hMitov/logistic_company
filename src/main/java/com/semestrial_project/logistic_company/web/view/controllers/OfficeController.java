package com.semestrial_project.logistic_company.web.view.controllers;

import com.semestrial_project.logistic_company.domain.services.OfficeService;
import com.semestrial_project.logistic_company.web.adapters.WebAdapter;
import com.semestrial_project.logistic_company.web.view.models.office.CreateOfficeView;
import com.semestrial_project.logistic_company.web.view.models.office.OfficeViewResponse;
import com.semestrial_project.logistic_company.web.view.models.office.UpdateOfficeView;
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
@RequestMapping("/offices")
public class OfficeController {

    private final OfficeService officeService;

    private final WebAdapter webAdapter;

    @GetMapping
    public String getAllOffices(Model model) {
        List<OfficeViewResponse> offices = officeService.getAllOffices().stream()
                .map(webAdapter::convertToOfficeViewResponse).collect(Collectors.toList());
        model.addAttribute("offices", offices);

        return "/offices/offices.html";
    }

    @GetMapping("/create-office")
    public String showCreateOfficeForm(Model model) {
        model.addAttribute("office", new CreateOfficeView());

        return "/offices/create-office";
    }

    @PostMapping("/create")
    public String createOffice(@Valid @ModelAttribute("office") CreateOfficeView createOfficeView,
                               BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "/offices/create-office";
        }
        officeService.addNewOffice(webAdapter.convertToCreateOffice(createOfficeView));

        return "redirect:/offices";
    }

    @GetMapping(path = "/offices/{id}")
    public OfficeViewResponse getOffice(@PathVariable Long id) throws Exception {
        return webAdapter.convertToOfficeViewResponse(officeService.getOfficeById(id));
    }

    @GetMapping("/edit-office/{id}")
    public String showEditOfficeForm(Model model, @PathVariable Long id) throws Exception {
        OfficeViewResponse office = webAdapter.convertToOfficeViewResponse(officeService.getOfficeById(id));
        model.addAttribute("office", office);

        return "/offices/edit-office";
    }

    @PatchMapping("/update/{id}")
    public String updateOffice(@PathVariable Long id, @ModelAttribute("office") UpdateOfficeView updateOfficeView) throws Exception {
        officeService.updateOffice(id, webAdapter.convertToUpdateOffice(updateOfficeView));

        return "redirect:/offices";
    }

    @GetMapping("/{id}/remove")
    public String removeOffice(@PathVariable Long id) throws Exception {
        officeService.removeOffice(id);
        return "redirect:/offices";
    }

}
