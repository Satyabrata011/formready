package com.satyabrata.formready.profile;

import com.satyabrata.formready.user.AppUser;
import com.satyabrata.formready.user.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
public class ProfileDetailsService {

    private final AppUserRepository users;
    private final ProfileRepository profiles;
    private final EducationRepository educations;
    private final WorkExperienceRepository workExperiences;
    private final ProfileSkillRepository skills;

    public ProfileDetailsService(
            AppUserRepository users,
            ProfileRepository profiles,
            EducationRepository educations,
            WorkExperienceRepository workExperiences,
            ProfileSkillRepository skills
    ) {
        this.users = users;
        this.profiles = profiles;
        this.educations = educations;
        this.workExperiences = workExperiences;
        this.skills = skills;
    }

    @Transactional(readOnly = true)
    public ProfileDetails getDetails(String email) {
        UUID profileId = findProfileForEmail(email).getId();
        return new ProfileDetails(
                educations.findAllByProfileIdOrderByGraduationYearDescInstitutionAsc(profileId),
                workExperiences.findAllByProfileIdOrderByStartDateDescCompanyAsc(profileId),
                skills.findAllByProfileIdOrderByNameAsc(profileId)
        );
    }

    @Transactional
    public void addEducation(String email, EducationForm form) {
        Profile profile = findProfileForEmail(email);
        educations.save(new Education(profile.getId(), form));
    }

    @Transactional
    public void addWorkExperience(String email, WorkExperienceForm form) {
        Profile profile = findProfileForEmail(email);
        workExperiences.save(new WorkExperience(profile.getId(), form));
    }

    @Transactional
    public void addSkill(String email, SkillForm form) {
        Profile profile = findProfileForEmail(email);
        String normalizedName = form.getName().trim();
        if (skills.existsByProfileIdAndNameIgnoreCase(profile.getId(), normalizedName)) {
            throw new DuplicateSkillException();
        }
        skills.save(new ProfileSkill(profile.getId(), normalizedName));
    }

    @Transactional
    public void deleteEducation(String email, UUID educationId) {
        Profile profile = findProfileForEmail(email);
        educations.findByIdAndProfileId(educationId, profile.getId()).ifPresent(educations::delete);
    }

    @Transactional
    public void deleteWorkExperience(String email, UUID workExperienceId) {
        Profile profile = findProfileForEmail(email);
        workExperiences.findByIdAndProfileId(workExperienceId, profile.getId()).ifPresent(workExperiences::delete);
    }

    @Transactional
    public void deleteSkill(String email, UUID skillId) {
        Profile profile = findProfileForEmail(email);
        skills.findByIdAndProfileId(skillId, profile.getId()).ifPresent(skills::delete);
    }

    private Profile findProfileForEmail(String email) {
        AppUser user = users.findByEmail(email.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new IllegalStateException("Signed-in account was not found"));
        return profiles.findByUserId(user.getId())
                .orElseThrow(() -> new ProfileRequiredException());
    }

    public static class DuplicateSkillException extends RuntimeException {
    }

    public static class ProfileRequiredException extends RuntimeException {
    }
}
