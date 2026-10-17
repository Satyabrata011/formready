package com.satyabrata.formready.application;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "checklist_items")
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "job_application_id", nullable = false)
    private UUID jobApplicationId;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(name = "is_required", nullable = false)
    private boolean required;

    @Column(nullable = false)
    private boolean completed;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    protected ChecklistItem() {
    }

    ChecklistItem(UUID jobApplicationId, String title, int displayOrder) {
        this.jobApplicationId = jobApplicationId;
        this.title = title;
        this.required = true;
        this.completed = false;
        this.displayOrder = displayOrder;
    }

    void toggle() {
        completed = !completed;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }
}
