CREATE TABLE checklist_items (
    id UUID PRIMARY KEY,
    job_application_id UUID NOT NULL,
    title VARCHAR(300) NOT NULL,
    is_required BOOLEAN NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INTEGER NOT NULL,
    CONSTRAINT fk_checklist_items_application FOREIGN KEY (job_application_id) REFERENCES job_applications (id) ON DELETE CASCADE
);

CREATE INDEX ix_checklist_items_application_id ON checklist_items (job_application_id);
