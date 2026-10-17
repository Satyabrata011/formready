package com.satyabrata.formready.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SkillForm {

    @NotBlank(message = "Enter a skill.")
    @Size(max = 100, message = "A skill must be 100 characters or fewer.")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
