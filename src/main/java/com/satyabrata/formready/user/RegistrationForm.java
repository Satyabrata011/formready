package com.satyabrata.formready.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationForm {

    @NotBlank(message = "Enter your name.")
    @Size(max = 100, message = "Your name must be 100 characters or fewer.")
    private String fullName;

    @NotBlank(message = "Enter your email address.")
    @Email(message = "Enter a valid email address.")
    @Size(max = 254, message = "Your email address is too long.")
    private String email;

    @NotBlank(message = "Create a password.")
    @Size(min = 12, max = 64, message = "Use a password between 12 and 64 characters.")
    private String password;

    @NotBlank(message = "Confirm your password.")
    private String passwordConfirmation;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
