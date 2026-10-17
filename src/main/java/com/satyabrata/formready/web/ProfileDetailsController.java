package com.satyabrata.formready.web;

import com.satyabrata.formready.profile.EducationForm;
import com.satyabrata.formready.profile.ProfileDetails;
import com.satyabrata.formready.profile.ProfileDetailsService;
import com.satyabrata.formready.profile.ProfileService;
import com.satyabrata.formready.profile.SkillForm;
import com.satyabrata.formready.profile.WorkExperienceForm;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class ProfileDetailsController {

    private final ProfileService profileService;
    private final ProfileDetailsService profileDetailsService;

    public ProfileDetailsController(ProfileService profileService, ProfileDetailsService profileDetailsService) {
        this.profileService = profileService;
        this.profileDetailsService = profileDetailsService;
    }

    @GetMapping("/profile/details")
    String details(Authentication authentication, Model model) {
        if (!profileService.hasProfile(authentication.getName())) {
            return "redirect:/profile";
        }
        populatePage(authentication.getName(), model);
        return "profile-details";
    }

    @PostMapping("/profile/education")
    String addEducation(Authentication authentication, @Valid @ModelAttribute EducationForm educationForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            populatePage(authentication.getName(), model);
            return "profile-details";
        }
        profileDetailsService.addEducation(authentication.getName(), educationForm);
        return "redirect:/profile/details?educationSaved";
    }

    @PostMapping("/profile/work-experience")
    String addWorkExperience(Authentication authentication, @Valid @ModelAttribute WorkExperienceForm workExperienceForm, BindingResult bindingResult, Model model) {
        if (workExperienceForm.getStartDate() != null && workExperienceForm.getEndDate() != null
                && workExperienceForm.getEndDate().isBefore(workExperienceForm.getStartDate())) {
            bindingResult.rejectValue("endDate", "date.order", "End date cannot be before start date.");
        }

        if (bindingResult.hasErrors()) {
            populatePage(authentication.getName(), model);
            return "profile-details";
        }
        profileDetailsService.addWorkExperience(authentication.getName(), workExperienceForm);
        return "redirect:/profile/details?experienceSaved";
    }

    @PostMapping("/profile/skills")
    String addSkill(Authentication authentication, @Valid @ModelAttribute SkillForm skillForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            populatePage(authentication.getName(), model);
            return "profile-details";
        }

        try {
            profileDetailsService.addSkill(authentication.getName(), skillForm);
        } catch (ProfileDetailsService.DuplicateSkillException exception) {
            bindingResult.rejectValue("name", "skill.duplicate", "This skill is already in your profile.");
            populatePage(authentication.getName(), model);
            return "profile-details";
        }
        return "redirect:/profile/details?skillSaved";
    }

    @PostMapping("/profile/education/{educationId}/delete")
    String deleteEducation(Authentication authentication, @PathVariable UUID educationId) {
        profileDetailsService.deleteEducation(authentication.getName(), educationId);
        return "redirect:/profile/details?educationDeleted";
    }

    @PostMapping("/profile/work-experience/{workExperienceId}/delete")
    String deleteWorkExperience(Authentication authentication, @PathVariable UUID workExperienceId) {
        profileDetailsService.deleteWorkExperience(authentication.getName(), workExperienceId);
        return "redirect:/profile/details?experienceDeleted";
    }

    @PostMapping("/profile/skills/{skillId}/delete")
    String deleteSkill(Authentication authentication, @PathVariable UUID skillId) {
        profileDetailsService.deleteSkill(authentication.getName(), skillId);
        return "redirect:/profile/details?skillDeleted";
    }

    private void populatePage(String email, Model model) {
        ProfileDetails details = profileDetailsService.getDetails(email);
        model.addAttribute("educations", details.educations());
        model.addAttribute("workExperiences", details.workExperiences());
        model.addAttribute("skills", details.skills());

        if (!model.containsAttribute("educationForm")) {
            model.addAttribute("educationForm", new EducationForm());
        }
        if (!model.containsAttribute("workExperienceForm")) {
            model.addAttribute("workExperienceForm", new WorkExperienceForm());
        }
        if (!model.containsAttribute("skillForm")) {
            model.addAttribute("skillForm", new SkillForm());
        }
    }
}
