# FormReady MVP design

## 1. Product statement

FormReady is a private, responsive web application that helps a person prepare a **job application** without repeatedly searching for details, forgetting documents, or missing a deadline.

The application is a preparation workspace. It does not submit applications to external job portals, make eligibility decisions, or give career, legal, or financial advice.

## 2. MVP success criteria

A new user can complete this journey on a phone or desktop browser:

```text
Register → Sign in → Complete profile → Create a job application
→ Receive a checklist → Review reusable answer cards
→ Track progress and deadline → Sign out
```

The MVP is successful when a user can prepare one job application using only FormReady, without needing to remember which details or documents are required.

## 3. Users and roles

### Visitor

A person who has not signed in. They can view the landing page, register, and sign in.

### Registered user

A person with a FormReady account. They can create, view, update, and delete **only their own** profile and job-application data.

There is no administrator role in version 1.

## 4. Scope

### Included in the MVP

- Email-and-password registration, sign-in, and sign-out.
- A private personal profile.
- Education, work-experience, skills, and useful links stored in the profile.
- Job application creation with company, role, URL, status, and deadline.
- A standard checklist generated for every new job application.
- Reusable answer cards generated from the saved profile.
- Progress tracking and visible deadline/overdue states.
- Responsive, keyboard-friendly pages.
- Automated UI and API checks for the core journey.

### Deferred after the MVP

- Chrome extension or automated third-party form filling.
- Automatic application submission.
- Document upload/storage, CV parsing, or OCR.
- Email, SMS, or push notifications. The MVP shows deadlines in-app; notification delivery is a later enhancement.
- AI-generated answers or career advice.
- Sharing profile data with another user.
- Government ID, banking, health, or other high-risk data storage.

## 5. Core user stories

| ID | User story | Priority |
| --- | --- | --- |
| US-01 | As a visitor, I want to create an account so that my application data remains private. | Must |
| US-02 | As a registered user, I want to sign in and sign out securely. | Must |
| US-03 | As a user, I want to save my contact details, education, work history, skills, and links once. | Must |
| US-04 | As a user, I want to create a job application with company, role, optional job URL, status, and deadline. | Must |
| US-05 | As a user, I want each new application to contain a preparation checklist. | Must |
| US-06 | As a user, I want reusable answer cards so I can easily copy saved information into an external application. | Must |
| US-07 | As a user, I want to update application and checklist progress so I can see what remains. | Must |
| US-08 | As a user, I want to see upcoming and overdue deadlines on my dashboard. | Must |
| US-09 | As a user, I want to edit or delete my application data. | Should |
| US-10 | As a user, I want to delete my account and associated data. | Later MVP-hardening |

## 6. Page inventory and navigation

```text
Landing page
 ├─ Register
 └─ Sign in
     └─ Dashboard
         ├─ Profile
         │   ├─ Contact details
         │   ├─ Education
         │   ├─ Work experience
         │   └─ Skills and links
         ├─ New job application
         └─ Application details
             ├─ Overview and progress
             ├─ Checklist
             └─ Answer cards
```

| Page | Main purpose | Key actions |
| --- | --- | --- |
| Landing | Explain the product and its privacy promise. | Register, sign in. |
| Register | Create a private account. | Submit name, email, password. |
| Sign in | Authenticate an existing user. | Sign in, view validation error. |
| Dashboard | Show applications and their next deadlines. | Open profile, create/open application. |
| Profile | Store reusable application details. | Create/edit contact, education, experience, skills, links. |
| New application | Capture the target role. | Save company, job title, URL, status, deadline. |
| Application details | Prepare one application. | Update status, check items, copy answer cards, edit/delete application. |

## 7. Initial data model

Use UUID primary keys and store timestamps in UTC.

```text
User 1 ── 1 Profile
Profile 1 ── * Education
Profile 1 ── * WorkExperience
Profile 1 ── * Skill
User 1 ── * JobApplication
JobApplication 1 ── * ChecklistItem
```

### User

| Field | Notes |
| --- | --- |
| id | UUID primary key |
| email | Unique, normalized, required |
| passwordHash | Never store a raw password |
| createdAt / updatedAt | UTC audit fields |

### Profile

| Field | Notes |
| --- | --- |
| id / userId | One profile owned by one user |
| firstName / lastName | Required after onboarding |
| phone | Optional |
| city / country | Optional; no full street address in version 1 |
| professionalSummary | Optional short text |
| linkedInUrl / portfolioUrl | Optional validated URLs |

### Education

| Field | Notes |
| --- | --- |
| id / profileId | Belongs to a profile |
| institution | Required |
| qualification | Required |
| fieldOfStudy | Optional |
| graduationYear | Optional |

### WorkExperience

| Field | Notes |
| --- | --- |
| id / profileId | Belongs to a profile |
| company | Required |
| jobTitle | Required |
| startDate / endDate | Optional; end date is empty for current role |
| description | Optional short text |

### Skill

| Field | Notes |
| --- | --- |
| id / profileId | Belongs to a profile |
| name | Required, unique within the profile |

### JobApplication

| Field | Notes |
| --- | --- |
| id / userId | Owned by one user |
| companyName | Required |
| jobTitle | Required |
| jobUrl | Optional valid URL |
| deadline | Optional date |
| status | `DRAFT`, `PREPARING`, `APPLIED`, `ARCHIVED` |
| createdAt / updatedAt | UTC audit fields |

### ChecklistItem

| Field | Notes |
| --- | --- |
| id / jobApplicationId | Belongs to one application |
| title | Required |
| required | `true` for generated MVP items |
| completed | Defaults to `false` |
| displayOrder | Controls UI order |

## 8. Standard generated checklist

When a user creates an application, FormReady generates these items:

1. Review job description
2. Prepare or update resume
3. Prepare cover letter
4. Review work experience and skills
5. Add portfolio or LinkedIn link if relevant
6. Submit application before deadline

The user can mark an item complete. Custom checklist items are deferred.

## 9. Backend route plan

The application will use server-rendered pages for the first UI. JSON routes exist for automated API checks and future integrations.

### HTML routes

| Route | Access | Purpose |
| --- | --- | --- |
| `GET /` | Public | Landing page |
| `GET, POST /register` | Public | Registration |
| `GET /login` | Public | Sign-in page |
| `GET /dashboard` | Authenticated | User dashboard |
| `GET, POST /profile` | Authenticated | Profile create/update |
| `GET, POST /applications/new` | Authenticated | Create application |
| `GET /applications/{id}` | Owner only | Application details |
| `POST /applications/{id}/status` | Owner only | Update status |
| `POST /checklist-items/{id}/toggle` | Owner only | Mark item complete/incomplete |

### JSON API routes

| Method | Route | Purpose |
| --- | --- | --- |
| `GET` | `/api/v1/profile` | Read current user's profile |
| `PUT` | `/api/v1/profile` | Update current user's profile |
| `GET` | `/api/v1/applications` | List current user's applications |
| `POST` | `/api/v1/applications` | Create an application and its checklist |
| `GET` | `/api/v1/applications/{id}` | Read an owned application |
| `PATCH` | `/api/v1/applications/{id}` | Update application status/deadline/details |
| `PATCH` | `/api/v1/checklist-items/{id}` | Update completion state for an owned item |

## 10. Acceptance criteria

### AC-01: secure registration

- A visitor can register with a unique valid email and a valid password.
- Duplicate emails are rejected with an understandable message.
- A password never appears in application logs, reports, or the UI after submission.

### AC-02: profile ownership

- An authenticated user can save their profile.
- Profile data is shown only to its owner.
- An unauthenticated visitor is redirected to sign in when opening `/profile`.

### AC-03: create an application

- A user must enter company name and job title.
- A saved application is visible on the dashboard.
- A new application defaults to status `DRAFT`.
- FormReady creates the six standard checklist items.

### AC-04: answer cards

- Answer cards display profile data without exposing password data.
- Blank optional profile fields show a helpful completion prompt, not an error.
- The user can copy an answer card using a visible copy button.

### AC-05: deadline awareness

- A dashboard shows applications ordered by nearest deadline.
- A past deadline has an obvious overdue label.
- An application with no deadline is allowed and appears after dated applications.

### AC-06: data isolation

- User A cannot read, update, or delete User B's application or checklist item by changing a URL or API identifier.

## 11. Initial BDD scenarios

```gherkin
@smoke @authentication
Scenario: A visitor creates a FormReady account
  Given a visitor is on the registration page
  When they register with valid details
  Then they should be signed in and see the dashboard

@smoke @profile
Scenario: A user saves a reusable profile
  Given a signed-in user is on the profile page
  When they save contact details, one education record, and one skill
  Then their profile details should be visible after they return to the dashboard

@smoke @application
Scenario: A user starts preparing a job application
  Given a signed-in user has a completed basic profile
  When they create an application for "QA Automation Engineer" at "Example Labs"
  Then the application should appear on the dashboard
  And it should contain the standard preparation checklist

@regression @deadline
Scenario: An overdue application is clearly identified
  Given a signed-in user has an application with a past deadline
  When they open the dashboard
  Then that application should be marked as overdue

@security @authorization
Scenario: A user cannot view another user's application
  Given two registered users have separate applications
  When the first user requests the second user's application URL
  Then access should be denied
```

## 12. Quality rules

- Mobile-first layout, tested at 320px width and desktop width.
- Every form field has a visible label and validation message.
- Keyboard navigation works for primary actions.
- No sensitive data appears in screenshots, logs, or test fixtures.
- Every page action has a stable `data-testid` where it is useful for UI tests.
- Page Objects hold locators and page actions; Cucumber steps hold business language and assertions.

## 13. Build sequence after approval

1. Create the Spring Boot Maven project.
2. Add PostgreSQL configuration and schema migrations.
3. Implement `User` and authentication.
4. Implement `Profile` and profile page.
5. Implement `JobApplication` and generated checklist.
6. Implement dashboard, answer cards, and deadline display.
7. Add Selenium/Cucumber/TestNG automation feature by feature.
8. Add JMeter API baseline after API routes are stable.

