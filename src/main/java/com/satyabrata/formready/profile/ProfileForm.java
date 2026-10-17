package com.satyabrata.formready.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProfileForm {

    @NotBlank(message = "Enter your first name.")
    @Size(max = 100, message = "Your first name must be 100 characters or fewer.")
    private String firstName;

    @NotBlank(message = "Enter your last name.")
    @Size(max = 100, message = "Your last name must be 100 characters or fewer.")
    private String lastName;

    @Size(max = 30, message = "Your phone number must be 30 characters or fewer.")
    private String phone;

    @Size(max = 100, message = "Your city must be 100 characters or fewer.")
    private String city;

    @Size(max = 100, message = "Your country must be 100 characters or fewer.")
    private String country;

    @Size(max = 1500, message = "Your summary must be 1,500 characters or fewer.")
    private String professionalSummary;

    @Size(max = 500, message = "Your LinkedIn link must be 500 characters or fewer.")
    private String linkedinUrl;

    @Size(max = 500, message = "Your portfolio link must be 500 characters or fewer.")
    private String portfolioUrl;

    static ProfileForm fromProfile(Profile profile) {
        ProfileForm form = new ProfileForm();
        form.firstName = profile.getFirstName();
        form.lastName = profile.getLastName();
        form.phone = profile.getPhone();
        form.city = profile.getCity();
        form.country = profile.getCountry();
        form.professionalSummary = profile.getProfessionalSummary();
        form.linkedinUrl = profile.getLinkedinUrl();
        form.portfolioUrl = profile.getPortfolioUrl();
        return form;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProfessionalSummary() {
        return professionalSummary;
    }

    public void setProfessionalSummary(String professionalSummary) {
        this.professionalSummary = professionalSummary;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }
}
