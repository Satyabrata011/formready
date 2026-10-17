package com.satyabrata.formready.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, UUID> {

    List<WorkExperience> findAllByProfileIdOrderByStartDateDescCompanyAsc(UUID profileId);

    Optional<WorkExperience> findByIdAndProfileId(UUID id, UUID profileId);
}
