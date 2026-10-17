CREATE TABLE educations (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL,
    institution VARCHAR(200) NOT NULL,
    qualification VARCHAR(200) NOT NULL,
    field_of_study VARCHAR(200),
    graduation_year INTEGER,
    CONSTRAINT fk_educations_profile FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE
);

CREATE TABLE work_experiences (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL,
    company VARCHAR(200) NOT NULL,
    job_title VARCHAR(200) NOT NULL,
    start_date DATE,
    end_date DATE,
    description VARCHAR(1000),
    CONSTRAINT fk_work_experiences_profile FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE
);

CREATE TABLE skills (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT uk_skills_profile_name UNIQUE (profile_id, name),
    CONSTRAINT fk_skills_profile FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE
);
