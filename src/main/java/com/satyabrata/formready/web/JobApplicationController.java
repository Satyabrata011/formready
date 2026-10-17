package com.satyabrata.formready.web;

import com.satyabrata.formready.application.ApplicationStatus;
import com.satyabrata.formready.application.JobApplication;
import com.satyabrata.formready.application.JobApplicationForm;
import com.satyabrata.formready.application.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping("/applications/new")
    String newApplication(Model model) {
        model.addAttribute("jobApplicationForm", new JobApplicationForm());
        model.addAttribute("statuses", ApplicationStatus.values());
        return "application-form";
    }

    @PostMapping("/applications")
    String createApplication(
            Authentication authentication,
            @Valid @ModelAttribute JobApplicationForm jobApplicationForm,
            BindingResult bindingResult,
            Model model
    ) {
        if (!jobApplicationService.isOptionalWebUrl(jobApplicationForm.getJobUrl())) {
            bindingResult.rejectValue("jobUrl", "url.invalid", "Enter a valid job web address.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", ApplicationStatus.values());
            return "application-form";
        }

        JobApplication application = jobApplicationService.create(authentication.getName(), jobApplicationForm);
        return "redirect:/applications/" + application.getId() + "?created";
    }

    @GetMapping("/applications/{applicationId}")
    String applicationDetails(Authentication authentication, @PathVariable UUID applicationId, Model model) {
        model.addAttribute("jobApplication", jobApplicationService.getApplication(authentication.getName(), applicationId));
        model.addAttribute("statuses", ApplicationStatus.values());
        return "application-details";
    }

    @PostMapping("/applications/{applicationId}/status")
    String updateStatus(
            Authentication authentication,
            @PathVariable UUID applicationId,
            @RequestParam ApplicationStatus status
    ) {
        jobApplicationService.updateStatus(authentication.getName(), applicationId, status);
        return "redirect:/applications/" + applicationId + "?statusUpdated";
    }
}
