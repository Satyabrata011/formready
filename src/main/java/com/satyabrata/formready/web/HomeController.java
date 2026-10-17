package com.satyabrata.formready.web;

import com.satyabrata.formready.profile.ProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProfileService profileService;

    public HomeController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/")
    String home() {
        return "home";
    }

    @GetMapping("/dashboard")
    String dashboard(Authentication authentication, Model model) {
        model.addAttribute("email", authentication.getName());
        model.addAttribute("profileCreated", profileService.hasProfile(authentication.getName()));
        return "dashboard";
    }
}
