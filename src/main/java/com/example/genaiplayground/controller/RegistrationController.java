package com.example.genaiplayground.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @GetMapping("/")
    public String registrationForm() {
        return "index";
    }

    @PostMapping("/register")
    public String registerUserAccount(Model model,
                                      @RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      @RequestParam("password_confirmation") String passwordConfirmation,
                                      @RequestParam("first_name") String firstName,
                                      @RequestParam("last_name") String lastName,
                                      @RequestParam(value = "terms_and_conditions", defaultValue = "false") boolean termsAndConditions) {

        return "index";
    }

}
