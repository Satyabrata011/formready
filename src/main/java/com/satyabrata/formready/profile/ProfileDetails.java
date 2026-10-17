package com.satyabrata.formready.profile;

import java.util.List;

public record ProfileDetails(
        List<Education> educations,
        List<WorkExperience> workExperiences,
        List<ProfileSkill> skills
) {
}
