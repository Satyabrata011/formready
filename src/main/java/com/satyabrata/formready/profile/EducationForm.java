package com.satyabrata.formready.profile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EducationForm {

    @NotBlank(message = "Enter the institution name.")
    @Size(max = 200, message = "The institution name must be 200 characters or fewer.")
    private String institution;

    @NotBlank(message = "Enter the qualification.")
    @Size(max = 200, message = "The qualification must be 200 characters or fewer.")
    private String qualification;

    @Size(max = 200, message = "The field of study must be 200 characters or fewer.")
    private String fieldOfStudy;

    @Min(value = 1900, message = "Enter a year from 1900 onwards.")
    @Max(value = 2100, message = "Enter a year no later than 2100.")
    private Integer graduationYear;

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }
}
