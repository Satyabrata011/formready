package com.satyabrata.formready;

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

}
