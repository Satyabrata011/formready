package com.satyabrata.formready.web;

import com.satyabrata.formready.user.RegistrationForm;
import com.satyabrata.formready.user.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    String registerForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    String register(@Valid @ModelAttribute RegistrationForm registrationForm, BindingResult bindingResult) {
        if (!registrationForm.getPassword().equals(registrationForm.getPasswordConfirmation())) {
            bindingResult.rejectValue("passwordConfirmation", "password.mismatch", "Passwords do not match.");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            registrationService.register(registrationForm);
        } catch (RegistrationService.EmailAlreadyRegisteredException exception) {
            bindingResult.rejectValue("email", "email.duplicate", "An account already uses this email address.");
            return "register";
        }

        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }
}
