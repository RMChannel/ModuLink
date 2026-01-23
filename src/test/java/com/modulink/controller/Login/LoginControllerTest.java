package com.modulink.controller.Login;

import com.modulink.Controller.Login.LoginController;
import com.modulink.Model.Email.EmailService;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import com.modulink.Model.Utente.PasswordUtility;
import com.modulink.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.mail.properties.mail.smtp.from=test@modulink.com")
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private ModuloService moduloService;

    @Test
    public void testLoginUnauthenticated() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/login"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testLoginAuthenticated() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    public void testForgotPasswordGetUnauthenticated() throws Exception {
        mockMvc.perform(get("/forgot-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/forgot-password"));
    }

    @Test
    public void testForgotPasswordPostValidEmail() throws Exception {
        String email = "test@example.com";
        UtenteEntity user = new UtenteEntity();
        user.setEmail(email);
        user.setNome("Test");
        user.setCognome("User");

        when(customUserDetailsService.findByEmail(email)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/forgot-password")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(view().name("login/otp-check"))
                .andExpect(model().attributeExists("confirmPasswordForm"));

        verify(emailService).sendEmail(any(SimpleMailMessage.class));
    }

    @Test
    public void testForgotPasswordPostInvalidEmailFormat() throws Exception {
        mockMvc.perform(post("/forgot-password")
                        .param("email", "invalid-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/forgot-password"))
                .andExpect(model().attribute("error", true))
                .andExpect(model().attributeExists("message"));
    }

    @Test
    public void testForgotPasswordPostEmailNotFound() throws Exception {
        String email = "notfound@example.com";
        when(customUserDetailsService.findByEmail(email)).thenReturn(Optional.empty());

        mockMvc.perform(post("/forgot-password")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(view().name("login/forgot-password"))
                .andExpect(model().attribute("error", true))
                .andExpect(model().attributeExists("message"));
    }

    @Test
    public void testResendOtp() throws Exception {
        String email = "test@example.com";
        UtenteEntity user = new UtenteEntity();
        user.setEmail(email);
        when(customUserDetailsService.findByEmail(email)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/resend-otp")
                        .param("email", email))
                .andExpect(status().isOk());

        verify(emailService).sendEmail(any(SimpleMailMessage.class));
    }

    @Test
    public void testConfirmNewPasswordSuccess() throws Exception {
        String email = "reset@example.com";
        String oldPasswordHash = PasswordUtility.hashPassword("OldPass123");
        UtenteEntity user = new UtenteEntity();
        user.setEmail(email);
        user.setNome("Reset");
        user.setCognome("User");
        user.setHash_password(oldPasswordHash);

        when(customUserDetailsService.findByEmail(email)).thenReturn(Optional.of(user));

        // 1. Trigger OTP generation via forgot-password
        mockMvc.perform(post("/forgot-password")
                        .param("email", email))
                .andExpect(status().isOk());

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailService).sendEmail(messageCaptor.capture());
        String emailText = messageCaptor.getValue().getText();

        String otp = emailText.substring(emailText.lastIndexOf(": ") + 2).trim();

        String newPassword = "NewPass123!";
        mockMvc.perform(post("/confirm-new-password")
                        .param("email", email)
                        .param("otp", otp)
                        .param("newPassword", newPassword)
                        .param("confirmNewPassword", newPassword))
                .andExpect(status().isOk())
                .andExpect(view().name("login/confirm-new-password"));

        ArgumentCaptor<UtenteEntity> userCaptor = ArgumentCaptor.forClass(UtenteEntity.class);
        verify(customUserDetailsService).aggiornaUtente(userCaptor.capture());
        
        UtenteEntity updatedUser = userCaptor.getValue();
        assertTrue(PasswordUtility.checkPassword(newPassword, updatedUser.getHash_password()));
    }

    @Test
    public void testConfirmNewPasswordSameAsOld() throws Exception {
        String email = "same@example.com";
        String password = "SamePassword123!";
        String passwordHash = PasswordUtility.hashPassword(password);
        
        UtenteEntity user = new UtenteEntity();
        user.setEmail(email);
        user.setNome("Same");
        user.setCognome("User");
        user.setHash_password(passwordHash);

        when(customUserDetailsService.findByEmail(email)).thenReturn(Optional.of(user));

        // 1. Trigger OTP generation
        mockMvc.perform(post("/forgot-password")
                        .param("email", email))
                .andExpect(status().isOk());

        // 2. Capture the OTP
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailService).sendEmail(messageCaptor.capture());
        String emailText = messageCaptor.getValue().getText();
        String otp = emailText.substring(emailText.lastIndexOf(": ") + 2).trim();

        // 3. Try to set the same password
        mockMvc.perform(post("/confirm-new-password")
                        .param("email", email)
                        .param("otp", otp)
                        .param("newPassword", password)
                        .param("confirmNewPassword", password))
                .andExpect(status().isOk())
                .andExpect(view().name("login/otp-check"))
                .andExpect(model().attributeHasFieldErrors("confirmPasswordForm", "newPassword"));
    }
    
    @Test
    public void testConfirmNewPasswordMismatch() throws Exception {
        mockMvc.perform(post("/confirm-new-password")
                        .param("email", "test@example.com")
                        .param("otp", "123456")
                        .param("newPassword", "Pass1")
                        .param("confirmNewPassword", "Pass2"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/otp-check"))
                .andExpect(model().attributeHasFieldErrors("confirmPasswordForm", "confirmNewPassword"));
    }
}
