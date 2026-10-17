package com.satyabrata.formready.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileSkillRepository extends JpaRepository<ProfileSkill, UUID> {

    List<ProfileSkill> findAllByProfileIdOrderByNameAsc(UUID profileId);

    boolean existsByProfileIdAndNameIgnoreCase(UUID profileId, String name);

    Optional<ProfileSkill> findByIdAndProfileId(UUID id, UUID profileId);
}
