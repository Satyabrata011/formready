package com.satyabrata.formready.application;

import com.satyabrata.formready.user.AppUser;
import com.satyabrata.formready.user.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class JobApplicationService {

    private final AppUserRepository users;
    private final JobApplicationRepository applications;

    public JobApplicationService(AppUserRepository users, JobApplicationRepository applications) {
        this.users = users;
        this.applications = applications;
    }

    @Transactional
    public JobApplication create(String email, JobApplicationForm form) {
        AppUser user = findUser(email);
        return applications.save(new JobApplication(user.getId(), form));
    }

    @Transactional(readOnly = true)
    public List<JobApplication> getApplications(String email) {
        return applications.findAllByUserIdOrderByCreatedAtDesc(findUser(email).getId());
    }

    @Transactional(readOnly = true)
    public JobApplication getApplication(String email, UUID applicationId) {
        return applications.findByIdAndUserId(applicationId, findUser(email).getId())
                .orElseThrow(ApplicationNotFoundException::new);
    }

    @Transactional
    public void updateStatus(String email, UUID applicationId, ApplicationStatus status) {
        JobApplication application = getApplication(email, applicationId);
        application.updateStatus(status);
    }

    public boolean isOptionalWebUrl(String value) {
        if (value == null || value.isBlank()) {
            return true;
        }

        try {
            URI uri = URI.create(value.trim());
            String scheme = uri.getScheme();
            return uri.getHost() != null && scheme != null
                    && ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme));
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private AppUser findUser(String email) {
        return users.findByEmail(email.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new IllegalStateException("Signed-in account was not found"));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ApplicationNotFoundException extends RuntimeException {
    }
}
