# FormReady project log

This is the persistent record for the project. Update it whenever a meaningful decision, implementation step, verification result, or scope change is made.

## Product definition

FormReady is a public, responsive web application that reduces the burden of repeated online applications. It helps people prepare information, track documents, save progress, and remember deadlines.

It is a user-facing product, not an internal company test framework and not an e-commerce automation project.

## Decisions made

| Decision | Rationale | Status |
| --- | --- | --- |
| Build a public web application first | It works across desktop and mobile browsers without installation. | Confirmed |
| Make a Chrome extension a later enhancement | An extension adds browser-specific scope and should only follow a proven core product. | Confirmed |
| Use Java 21 and Spring Boot | Fits the planned Java automation ecosystem and supports a maintainable backend. | Confirmed |
| Use Selenium with Page Object Model | Keeps UI automation readable and resilient as the UI changes. | Confirmed |
| Keep users in control of external submissions | FormReady will prepare data and checklists but will not auto-submit third-party forms. | Confirmed |
| Do not store highly sensitive identity documents in version 1 | Reduces privacy and security risk while the product foundation is built. | Confirmed |

## Initial version (MVP)

1. Account registration and sign-in.
2. Personal profile: contact details, address, education, experience, skills, and links.
3. Application templates, starting with Job Application.
4. Document checklist for each application.
5. Reusable answer cards that users can copy.
6. Application progress status and deadline reminder.
7. Responsive browser experience.

## Planned verification

- Cucumber scenarios for key user journeys.
- TestNG smoke and regression suites via `testng.xml`.
- Selenium Page Objects for all application screens.
- JSON fixtures for profiles and application templates.
- YAML environment configuration.
- JMeter low-volume API baseline checks for profile, checklist, and application endpoints.

## Explicit non-goals for version 1

- Automated submission to job boards or other third-party services.
- Browser extension.
- Storage of government ID, banking details, or health records.
- Legal, immigration, financial, or admissions advice.

## Work log

| Date | Work completed | Result |
| --- | --- | --- |
| 2026-10-14 | Defined FormReady as the selected project and created the persistent project record. | Complete |
| 2026-10-14 | Created the setup and development roadmap, including tool setup, architecture, build order, testing, and deployment procedure. | Complete |
| 2026-10-14 | Audited the local Windows environment. JDK 21.0.1, Git, IntelliJ IDEA, and Chrome are present; `JAVA_HOME`, Maven CLI, and JMeter still need manual setup. Docker is deferred. | Complete |
| 2026-10-14 | Verified Phase 1 manual setup: Java 21.0.1, Maven 3.10.0, Git identity, IntelliJ IDEA, Chrome, and JMeter are ready. Docker remains intentionally deferred. | Complete |
| 2026-10-14 | Created the MVP design: product boundary, users, pages, data model, routes, acceptance criteria, and initial BDD scenarios. | Complete |
| 2026-10-17 | Implemented the public FormReady landing page, its access rule, responsive styling, and a MockMvc verification test. | Complete |

## Next step

Run the landing-page test and start the application. Then implement user registration and application-specific authentication.
