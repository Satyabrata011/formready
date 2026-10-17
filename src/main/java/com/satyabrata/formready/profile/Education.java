package com.satyabrata.formready.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "educations")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "profile_id", nullable = false)
    private UUID profileId;

    @Column(nullable = false, length = 200)
    private String institution;

    @Column(nullable = false, length = 200)
    private String qualification;

    @Column(name = "field_of_study", length = 200)
    private String fieldOfStudy;

    @Column(name = "graduation_year")
    private Integer graduationYear;

    protected Education() {
    }

    Education(UUID profileId, EducationForm form) {
        this.profileId = profileId;
        institution = form.getInstitution().trim();
        qualification = form.getQualification().trim();
        fieldOfStudy = optionalValue(form.getFieldOfStudy());
        graduationYear = form.getGraduationYear();
    }

    private String optionalValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    public UUID getId() {
        return id;
    }

    public String getInstitution() {
        return institution;
    }

    public String getQualification() {
        return qualification;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }
}
