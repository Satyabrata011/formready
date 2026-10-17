package com.satyabrata.formready.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class JobApplicationForm {

    @NotBlank(message = "Enter the company name.")
    @Size(max = 200, message = "The company name must be 200 characters or fewer.")
    private String companyName;

    @NotBlank(message = "Enter the job title.")
    @Size(max = 200, message = "The job title must be 200 characters or fewer.")
    private String jobTitle;

    @Size(max = 500, message = "The job link must be 500 characters or fewer.")
    private String jobUrl;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deadline;

    @NotNull(message = "Choose an application status.")
    private ApplicationStatus status = ApplicationStatus.DRAFT;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
