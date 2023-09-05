package com.semestrial_project.logistic_company.web.view.controllers;

import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class DomainExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public String handleException(DomainException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "/errors/error";
    }
}
