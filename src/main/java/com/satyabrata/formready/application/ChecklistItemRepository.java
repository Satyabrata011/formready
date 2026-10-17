package com.satyabrata.formready.application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, UUID> {

    List<ChecklistItem> findAllByJobApplicationIdOrderByDisplayOrderAsc(UUID jobApplicationId);

    Optional<ChecklistItem> findByIdAndJobApplicationId(UUID id, UUID jobApplicationId);
}
