package com.satyabrata.formready.application;

import com.satyabrata.formready.profile.Education;
import com.satyabrata.formready.profile.ProfileDetails;
import com.satyabrata.formready.profile.ProfileDetailsService;
import com.satyabrata.formready.profile.ProfileForm;
import com.satyabrata.formready.profile.ProfileService;
import com.satyabrata.formready.profile.ProfileSkill;
import com.satyabrata.formready.profile.WorkExperience;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerCardService {

    private final ProfileService profileService;
    private final ProfileDetailsService profileDetailsService;

    public AnswerCardService(ProfileService profileService, ProfileDetailsService profileDetailsService) {
        this.profileService = profileService;
        this.profileDetailsService = profileDetailsService;
    }

    public List<AnswerCard> getCards(String email) {
        if (!profileService.hasProfile(email)) {
            return List.of();
        }

        ProfileForm profile = profileService.getProfileForm(email);
        ProfileDetails details = profileDetailsService.getDetails(email);
        List<AnswerCard> cards = new ArrayList<>();

        addIfPresent(cards, "Full name", joinNonBlank(" ", profile.getFirstName(), profile.getLastName()));
        addIfPresent(cards, "Phone number", profile.getPhone());
        addIfPresent(cards, "Location", joinNonBlank(", ", profile.getCity(), profile.getCountry()));
        addIfPresent(cards, "Professional summary", profile.getProfessionalSummary());
        addIfPresent(cards, "LinkedIn", profile.getLinkedinUrl());
        addIfPresent(cards, "Portfolio", profile.getPortfolioUrl());
        addIfPresent(cards, "Education", formatEducation(details.educations()));
        addIfPresent(cards, "Work experience", formatWorkExperience(details.workExperiences()));
        addIfPresent(cards, "Skills", details.skills().stream().map(ProfileSkill::getName).collect(Collectors.joining(", ")));

        return cards;
    }

    private void addIfPresent(List<AnswerCard> cards, String label, String value) {
        if (value != null && !value.isBlank()) {
            cards.add(new AnswerCard(label, value));
        }
    }

    private String joinNonBlank(String separator, String... values) {
        return java.util.Arrays.stream(values)
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.joining(separator));
    }

    private String formatEducation(List<Education> educations) {
        return educations.stream()
                .map(education -> joinNonBlank(" — ", education.getQualification(), education.getInstitution()))
                .collect(Collectors.joining("\n"));
    }

    private String formatWorkExperience(List<WorkExperience> workExperiences) {
        return workExperiences.stream()
                .map(experience -> joinNonBlank(" at ", experience.getJobTitle(), experience.getCompany()))
                .collect(Collectors.joining("\n"));
    }
}
