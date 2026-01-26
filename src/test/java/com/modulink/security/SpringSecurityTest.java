package com.modulink.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SpringSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void accessHome_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void accessLogin_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void accessRegister_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    void accessDashboard_ShouldRedirect() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void accessContactUs_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/contactus"))
                .andExpect(status().isOk());
    }

    @Test
    void accessPrivacy_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/privacy"))
                .andExpect(status().isOk());
    }

    @Test
    void accessTermini_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/termini"))
                .andExpect(status().isOk());
    }

    @Test
    void accessForgotPassword_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/forgot-password"))
                .andExpect(status().isOk());
    }

    @Test
    void accessNews_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/news"))
                .andExpect(status().isOk());
    }

    @Test
    void accessSupporto_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/supporto"))
                .andExpect(status().isOk());
    }

    @Test
    void accessPacchetti_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/pacchetti"))
                .andExpect(status().isOk());
    }

    @Test
    void accessManuale_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/manuale"))
                .andExpect(status().isOk());
    }
}
