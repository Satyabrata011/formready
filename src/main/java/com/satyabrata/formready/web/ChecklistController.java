package com.satyabrata.formready.web;

import com.satyabrata.formready.application.JobApplicationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class ChecklistController {

    private final JobApplicationService jobApplicationService;

    public ChecklistController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping("/applications/{applicationId}/checklist-items/{checklistItemId}/toggle")
    String toggleChecklistItem(
            Authentication authentication,
            @PathVariable UUID applicationId,
            @PathVariable UUID checklistItemId
    ) {
        jobApplicationService.toggleChecklistItem(authentication.getName(), applicationId, checklistItemId);
        return "redirect:/applications/" + applicationId + "?checklistUpdated";
    }
}
