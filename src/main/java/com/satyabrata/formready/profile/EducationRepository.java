package com.satyabrata.formready.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EducationRepository extends JpaRepository<Education, UUID> {

    List<Education> findAllByProfileIdOrderByGraduationYearDescInstitutionAsc(UUID profileId);

    Optional<Education> findByIdAndProfileId(UUID id, UUID profileId);
}
