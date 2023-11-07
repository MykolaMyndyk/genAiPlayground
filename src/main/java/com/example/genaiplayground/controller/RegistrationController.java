package com.example.genaiplayground.controller;

import com.example.genaiplayground.entity.AuthUser;
import com.example.genaiplayground.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        if(email.length() < 6 || email.length() > 30) {
            model.addAttribute("emailError", "Email Address should be more than 6 and less than 30 characters.");
            return "index";
        }

        if(password.length() < 8) {
            model.addAttribute("passwordError", "Password should be at least 8 characters.");
            return "index";
        }

        if(!(password.matches(".*[a-z].*") && password.matches(".*[A-Z].*") && password.matches(".*\\d.*"))) {
            model.addAttribute("passwordError", "Password should contain at least one lowercase, one uppercase and one numeric character.");
            return "index";
        }

        if(password.contains(email.substring(0, 3))) {
            model.addAttribute("passwordError", "Password should not contain a string of three or more consecutive characters that can be found in any part of the Email Address.");
            return "index";
        }

        if(lastName.isEmpty()) {
            model.addAttribute("lastNameError", "Last Name should not be empty.");
            return "index";
        }

        if(!termsAndConditions) {
            model.addAttribute("termsAndConditionsError", "Please read and accept the Terms of Service and Privacy Policy.");
            return "index";
        }

        AuthUser existingUser = authUserRepository.findByEmail(email);
        if (existingUser != null) {
            model.addAttribute("emailError", "Email already in use.");
            return "index";
        }

        AuthUser authUser = new AuthUser();
        authUser.setEmail(email);
        authUser.setPassword(password);
        authUser.setFirstName(firstName);
        authUser.setLastName(lastName);
        authUserRepository.save(authUser);

        return "endRegister";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<AuthUser>> getAllUsers() {
        List<AuthUser> users = authUserRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
