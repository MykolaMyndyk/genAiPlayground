package com.example.genaiplayground.controller;

import com.example.genaiplayground.model.AuthUser;
import com.example.genaiplayground.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.regex.Pattern;

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

        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        boolean isValidEmail = email.length() >= 6 && email.length() <= 30;
        boolean isValidPassword = password.equals(passwordConfirmation) && Pattern.matches(passwordPattern, password);
        boolean isValidFirstName = firstName.length() >= 1;
        boolean isValidLastName = lastName.length() >= 1;

        AuthUser existingUser = authUserRepository.findByEmail(email);
        if (existingUser != null) {
            model.addAttribute("emailError", "Email already in use");
            return "index";
        }

        if (!isValidEmail) {
            model.addAttribute("emailError", "Email should be more than 6 and less than 30 characters long");
        }
        if (!isValidPassword) {
            model.addAttribute("passwordError", "Password should contain at least 8 characters, one uppercase letter, one lowercase letter and one digit");
        }
        if (!isValidFirstName) {
            model.addAttribute("firstNameError", "First Name should be at least 1 character");
        }
        if (!isValidLastName) {
            model.addAttribute("lastNameError", "Last Name should be at least 1 character");
        }
        if (!termsAndConditions) {
            model.addAttribute("termsAndConditionsError", "You must accept terms and conditions");
        }

        if (isValidEmail && isValidPassword && isValidFirstName && isValidLastName && termsAndConditions) {
            AuthUser authUser = new AuthUser();
            authUser.setEmail(email);
            authUser.setPassword(password);
            authUser.setFirstName(firstName);
            authUser.setLastName(lastName);
            authUserRepository.save(authUser);
            return "end_register";
        }

        return "index";
    }

    @GetMapping("/end_register")
    public String endRegistration() {
        return "end_register";
    }

    @GetMapping("/users")
    @ResponseBody
    public List<AuthUser> getAllUsers() {
        return authUserRepository.findAll();
    }

}




