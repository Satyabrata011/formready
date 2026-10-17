package com.satyabrata.formready.web;

import com.satyabrata.formready.profile.ProfileForm;
import com.satyabrata.formready.profile.ProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    String profile(Authentication authentication, Model model) {
        model.addAttribute("profileForm", profileService.getProfileForm(authentication.getName()));
        return "profile";
    }

    @PostMapping("/profile")
    String saveProfile(Authentication authentication, @Valid @ModelAttribute ProfileForm profileForm, BindingResult bindingResult) {
        validateWebUrl(profileForm.getLinkedinUrl(), "linkedinUrl", "Enter a valid LinkedIn web address.", bindingResult);
        validateWebUrl(profileForm.getPortfolioUrl(), "portfolioUrl", "Enter a valid portfolio web address.", bindingResult);

        if (bindingResult.hasErrors()) {
            return "profile";
        }

        profileService.saveProfile(authentication.getName(), profileForm);
        return "redirect:/dashboard?profileSaved";
    }

    private void validateWebUrl(String value, String field, String message, BindingResult bindingResult) {
        if (!profileService.isOptionalWebUrl(value)) {
            bindingResult.rejectValue(field, "url.invalid", message);
        }
    }
}
