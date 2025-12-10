package com.modulink;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void unauthenticatedUserShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void loginWithInvalidUserShouldBeUnauthenticated() throws Exception {
        mockMvc.perform(formLogin("/login").user("invalid@test.com").password("invalidpassword"))
                .andExpect(unauthenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }
    
    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void authenticatedUserShouldAccessDashboard() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk());
    }
}
