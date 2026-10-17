package com.satyabrata.formready.application;

public enum ApplicationStatus {
    DRAFT("Draft"),
    PREPARING("Preparing"),
    APPLIED("Applied"),
    ARCHIVED("Archived");

    private final String label;

    ApplicationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
