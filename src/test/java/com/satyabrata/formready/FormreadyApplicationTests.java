package com.satyabrata.formready;

import com.satyabrata.formready.profile.Profile;
import com.satyabrata.formready.profile.ProfileRepository;
import com.satyabrata.formready.profile.EducationRepository;
import com.satyabrata.formready.profile.ProfileSkillRepository;
import com.satyabrata.formready.profile.WorkExperienceRepository;
import com.satyabrata.formready.user.AppUser;
import com.satyabrata.formready.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class FormreadyApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository users;

    @Autowired
    private ProfileRepository profiles;

    @Autowired
    private EducationRepository educations;

    @Autowired
    private WorkExperienceRepository workExperiences;

    @Autowired
    private ProfileSkillRepository skills;

	@Test
	void contextLoads() {
	}

    @Test
    void homePageIsPublicAndShowsFormReady() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("FormReady")));
    }

    @Test
    void visitorCanRegisterThenSignInAndSeeTheirDashboard() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("fullName", "Asha User")
                        .param("email", "ASHA.EXAMPLE@EMAIL.COM")
                        .param("password", "correct-horse-88")
                        .param("passwordConfirmation", "correct-horse-88"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        AppUser user = users.findByEmail("asha.example@email.com").orElseThrow();
        assertThat(user.getPasswordHash()).startsWith("$2");

        MvcResult login = mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "asha.example@email.com")
                        .param("password", "correct-horse-88"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andReturn();

        mockMvc.perform(get("/dashboard").session((MockHttpSession) login.getRequest().getSession()))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("asha.example@email.com")));
    }

    @Test
    void signedInUserCanSaveAndReviewTheirOwnProfile() throws Exception {
        MockHttpSession session = registerAndSignIn("Profile User", "profile.owner@email.com", "secure-password-34");

        mockMvc.perform(post("/profile")
                        .session(session)
                        .with(csrf())
                        .param("firstName", "Profile")
                        .param("lastName", "User")
                        .param("phone", "+91 98765 43210")
                        .param("city", "Bhubaneswar")
                        .param("country", "India")
                        .param("professionalSummary", "Automation tester focused on useful, reliable web products.")
                        .param("linkedinUrl", "https://www.linkedin.com/in/example")
                        .param("portfolioUrl", "https://example.dev"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard?profileSaved"));

        AppUser user = users.findByEmail("profile.owner@email.com").orElseThrow();
        Profile profile = profiles.findByUserId(user.getId()).orElseThrow();
        assertThat(profile.getCity()).isEqualTo("Bhubaneswar");
        assertThat(profile.getPortfolioUrl()).isEqualTo("https://example.dev");

        mockMvc.perform(get("/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Bhubaneswar")));
    }

    @Test
    void signedInUserCanAddCareerDetailsToTheirProfile() throws Exception {
        MockHttpSession session = registerAndSignIn("Career User", "career.owner@email.com", "secure-password-56");
        saveBasicProfile(session, "Career", "User");

        mockMvc.perform(post("/profile/education")
                        .session(session)
                        .with(csrf())
                        .param("institution", "Bhubaneswar Technical Institute")
                        .param("qualification", "Bachelor of Technology")
                        .param("fieldOfStudy", "Computer Science")
                        .param("graduationYear", "2022"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/details?educationSaved"));

        mockMvc.perform(post("/profile/work-experience")
                        .session(session)
                        .with(csrf())
                        .param("company", "Example Labs")
                        .param("jobTitle", "QA Automation Engineer")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "")
                        .param("description", "Built reliable test automation."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/details?experienceSaved"));

        mockMvc.perform(post("/profile/skills")
                        .session(session)
                        .with(csrf())
                        .param("name", "Selenium"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/details?skillSaved"));

        assertThat(educations.count()).isEqualTo(1);
        assertThat(workExperiences.count()).isEqualTo(1);
        assertThat(skills.count()).isEqualTo(1);

        mockMvc.perform(get("/profile/details").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("profile-details"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Bhubaneswar Technical Institute")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("QA Automation Engineer")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Selenium")));
    }

    private MockHttpSession registerAndSignIn(String fullName, String email, String password) throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("fullName", fullName)
                        .param("email", email)
                        .param("password", password)
                        .param("passwordConfirmation", password))
                .andExpect(status().is3xxRedirection());

        MvcResult login = mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", email)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        return (MockHttpSession) login.getRequest().getSession();
    }

    private void saveBasicProfile(MockHttpSession session, String firstName, String lastName) throws Exception {
        mockMvc.perform(post("/profile")
                        .session(session)
                        .with(csrf())
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("phone", "")
                        .param("city", "")
                        .param("country", "")
                        .param("professionalSummary", "")
                        .param("linkedinUrl", "")
                        .param("portfolioUrl", ""))
                .andExpect(status().is3xxRedirection());
    }

}
