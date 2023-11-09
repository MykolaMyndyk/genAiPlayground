package com.example.genaiplayground.controller;

import com.example.genaiplayground.entity.AuthUser;
import com.example.genaiplayground.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class RegistrationController {

    @Autowired
    private AuthUserRepository authUserRepository;

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

        boolean hasError = false;

        // Check if email already exists
        Optional<AuthUser> existingUser = authUserRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            model.addAttribute("emailError", "Email already in use");
            hasError = true;
        }

        // Email validation
        if (!hasError && (email.length() <= 6 || email.length() >= 30)) {
            model.addAttribute("emailError", "Email must be between 7 and 29 characters");
            hasError = true;
        }

        // Password validation
        if (password.length() < 8 || !password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*") || !password.matches(".*[0-9].*")) {
            model.addAttribute("passwordError", "Password must be at least 8 characters and include a mix of upper, lower, and numbers");
            hasError = true;
        }

        // First name validation
        if (firstName.length() < 1) {
            model.addAttribute("firstNameError", "First Name is required");
            hasError = true;
        }

        // Last name validation
        if (lastName.length() < 1) {
            model.addAttribute("lastNameError", "Last Name is required");
            hasError = true;
        }

        // Terms and Conditions validation
        if (!termsAndConditions) {
            model.addAttribute("termsError", "You must accept the terms and conditions");
            hasError = true;
        }

        if (!hasError) {
            AuthUser newUser = new AuthUser();
            newUser.setEmail(email);
            newUser.setPassword(password); // In a real application, you should encrypt the password before saving
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            authUserRepository.save(newUser);

            return "end_register";
        } else {
            return "index";
        }
    }

    @GetMapping("/users")
    @ResponseBody
    public List<AuthUser> getAllUsers() {
        return authUserRepository.findAll();
    }
}
