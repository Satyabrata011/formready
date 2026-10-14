# FormReady roadmap

This roadmap separates the preparation work from the development work. Complete each phase in order; do not begin the next phase until its verification check passes.

## Part 1 — System, account, and project setup

### Phase 0: confirm the product boundary

**Goal:** Keep the first release small, useful, and safe.

The first release will let a user create an account, save a non-sensitive profile, prepare a job application checklist, copy reusable answers, track progress, and save a deadline.

**Do not build yet:** browser extension, automatic external form submission, document uploads, public sharing, payment features, or AI-generated advice.

**Verification:** The MVP features and non-goals match `docs/PROJECT_LOG.md`.

### Phase 1: install local development tools

| Item | Required version / choice | Why it is needed | Verification |
| --- | --- | --- | --- |
| Java Development Kit | JDK 21 (LTS) | Builds and runs the Spring Boot application and Java test framework. | `java -version` shows 21. |
| Maven | Current stable 3.9+ | Manages Java dependencies and runs tests. | `mvn -version` succeeds. |
| Git | Current stable | Tracks work and publishes the repository. | `git --version` succeeds. |
| IDE | IntelliJ IDEA Community is recommended | Develops, debugs, and runs Java/Spring projects. | A Maven project opens successfully. |
| Google Chrome | Current stable | Primary Selenium test browser. | Chrome opens normally. |
| ChromeDriver | Selenium Manager preferred | Selenium obtains a compatible driver automatically. | A sample browser test starts. |
| Apache JMeter | Current stable | Runs API baseline checks. | JMeter opens and a sample plan runs. |
| Postman or Bruno | Optional but recommended | Manually explores and verifies APIs during development. | A sample HTTP request succeeds. |
| Docker Desktop | Optional in first week; recommended before deployment | Runs the database and application consistently. | `docker --version` succeeds. |

**Procedure:** Install JDK first, then Maven, Git, IDE, Chrome, JMeter, and Docker. Restart the terminal after changing system environment variables.

### Phase 2: create essential accounts

| Account / service | Use | When needed |
| --- | --- | --- |
| GitHub | Source code, issues, Actions, and public portfolio | Immediately |
| GitHub Container Registry or Docker Hub | Optional container image publishing | Before deployment |
| Neon, Supabase, or another managed PostgreSQL provider | Hosted database | Before public deployment |
| Render, Railway, or Fly.io | Application hosting | Before public deployment |
| Email delivery provider | Deadline reminders | After core MVP works |

**Security procedure:** Enable multi-factor authentication for every account. Keep credentials in a password manager. Never commit passwords, database URLs, API keys, or email credentials.

### Phase 3: create the repository foundation

**Required files:**

```text
README.md
.gitignore
.env.example
docs/PROJECT_LOG.md
docs/ROADMAP.md
LICENSE
```

**Recommended Git branches:**

```text
main        → reviewed, deployable code
develop     → integration branch during early work
feature/*   → one focused feature at a time
```

**Verification:** A clean clone of the repository contains no secrets, build output, or local database files.

### Phase 4: select the local architecture

| Layer | Version 1 choice | Responsibility |
| --- | --- | --- |
| Frontend | Thymeleaf, HTML, CSS, and minimal JavaScript | Accessible responsive pages without a separate JavaScript application. |
| Backend | Java 21, Spring Boot, Spring MVC | Web pages, validation, business rules, and REST endpoints. |
| Database | PostgreSQL | Users, profile fields, applications, checklist items, and deadlines. |
| Database access | Spring Data JPA | Repository layer and schema migrations. |
| Authentication | Spring Security with email/password | Private user accounts and access control. |
| UI tests | Selenium + Cucumber + TestNG | End-to-end browser testing using Page Object Model. |
| API baseline | JMeter | Low-volume performance and availability checks for FormReady APIs. |

**Why this choice:** It keeps the first project mostly Java, which is easier to learn, demonstrate, and automate with your selected stack.

### Phase 5: baseline quality configuration

Set these rules before writing features:

- Format and validate code before each commit.
- Use environment variables for every secret.
- Use a separate local development database; never use production data locally.
- Give every feature an acceptance criterion and at least one automated test.
- Add a GitHub Actions workflow after the first working test.
- Maintain `docs/PROJECT_LOG.md` after every completed phase.

**Verification:** The application can start locally with example configuration only; no real secret is required.

## Part 2 — Development order and procedure

### Phase 6: design before code

**Deliverables:**

1. User journeys.
2. Page inventory and navigation map.
3. Database entities and relationships.
4. REST endpoint list.
5. Acceptance criteria.
6. Initial Cucumber feature files.

**Core user journey:**

```text
Register → Sign in → Create profile → Select Job Application → Review checklist
→ Copy answer cards → Save application progress → Set deadline → Sign out
```

**Verification:** Each screen has a clear purpose and each user action maps to data that must be stored.

### Phase 7: build backend foundation first

**Build in this order:**

1. Create Spring Boot Maven project.
2. Add PostgreSQL connection and migration tooling.
3. Define database entities: `User`, `Profile`, `Application`, `ChecklistItem`, and `Deadline`.
4. Implement authentication and authorization.
5. Implement profile create, read, update, and delete operations.
6. Add request validation and safe error messages.
7. Write backend unit and integration tests.

**Why backend first:** The frontend needs reliable data models and APIs. Building them first avoids creating screens that cannot store real user data.

**Verification:** A user can register, sign in, save a profile, sign out, and see only their own profile after signing in again.

### Phase 8: build the first frontend journey

**Build pages in this order:**

1. Landing page.
2. Register and sign-in pages.
3. Dashboard.
4. Profile editor.
5. Application template selector.
6. Job-application checklist page.
7. Reusable answer-card page.
8. Application-progress and deadline page.

**Responsive procedure:** Implement each page for a small mobile screen first, then verify tablet and desktop layouts.

**Verification:** A user can complete the core user journey without opening developer tools or manually editing data.

### Phase 9: add application preparation logic

**Build in this order:**

1. Create the Job Application template.
2. Link a profile to an application instance.
3. Generate checklist items from the template.
4. Generate read-only answer cards from saved profile data.
5. Let users mark checklist and application progress.
6. Save a deadline and show it on the dashboard.
7. Add reminder logic after the core workflow is stable.

**Verification:** Two different users see separate profiles, applications, checklists, and deadlines.

### Phase 10: build automation alongside completed features

Create automation after each screen is stable; do not leave all testing until the end.

| Completed feature | Automation to add |
| --- | --- |
| Authentication | Valid sign-in, invalid password, sign-out scenarios |
| Profile | Create, edit, validation-error, and privacy scenarios |
| Job application | Template selection and checklist-generation scenario |
| Answer cards | Correct profile values displayed; no other user's data visible |
| Progress and deadlines | Save, edit, and overdue-state scenarios |

**Framework procedure:**

1. Write the Cucumber scenario.
2. Create or extend a Page Object.
3. Write the step definition.
4. Add it to the TestNG smoke or regression suite.
5. Capture screenshot and logs on failure.

**Verification:** Smoke tests pass locally in Chrome; regression tests pass before merging to `main`.

### Phase 11: API baseline and security checks

**JMeter scope:** Test only FormReady APIs, never unrelated public services.

1. Create a low-volume JMeter plan for sign-in, profile retrieval, checklist retrieval, and application saving.
2. Validate response status, response shape, and a realistic response-time threshold.
3. Run the plan locally first.
4. Run it against staging before a production release.

**Security checklist:**

- Passwords are hashed by Spring Security.
- User A cannot read or modify User B's data.
- Sensitive values never appear in logs or screenshots.
- Production uses HTTPS.
- Accounts support data deletion.
- Version 1 stores no government-ID, financial, or health documents.

### Phase 12: continuous integration, deployment, and portfolio polish

**CI procedure:**

1. GitHub Actions builds the project on each pull request.
2. Run backend unit/integration tests.
3. Run headless Selenium smoke tests against a test environment.
4. Publish test reports as workflow artifacts.
5. Protect the `main` branch so failing tests cannot merge.

**Deployment procedure:**

1. Create a staging environment and database.
2. Deploy the application to staging.
3. Run smoke tests and JMeter API baseline.
4. Review logs and test reports.
5. Deploy the approved build to production.
6. Verify the public URL on desktop and mobile.

**Portfolio checklist:**

- Clear README with problem, screenshots, architecture, and run instructions.
- Architecture diagram and user-flow image.
- Sample Cucumber report screenshot.
- GitHub Actions badge.
- No secrets or personal data in the repository.

## Recommended milestones

| Milestone | Outcome |
| --- | --- |
| M1 — Environment ready | Tools, GitHub repository, documentation, and local Spring Boot app work. |
| M2 — Secure profile | Registration, sign-in, and profile CRUD work. |
| M3 — Useful MVP | Job Application checklist, answer cards, progress, and deadline work. |
| M4 — Automated quality | Selenium/Cucumber/TestNG tests and JMeter baseline run reliably. |
| M5 — Live portfolio | CI pipeline, deployed staging/production app, polished README, and demo evidence are ready. |

## Definition of done for each feature

A feature is complete only when:

1. Its acceptance criteria are written.
2. The backend validation and authorization are implemented.
3. The UI works on mobile and desktop layouts.
4. Error and empty states are understandable.
5. At least one automated test covers the normal path.
6. Relevant regression tests pass.
7. The project log is updated.

