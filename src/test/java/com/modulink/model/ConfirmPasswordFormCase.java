package com.modulink.model;

import com.modulink.Controller.Login.ConfirmPasswordForm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ConfirmPasswordFormCase {

    @Test
    public void TestGetterAndSetter() {
        ConfirmPasswordForm form = new ConfirmPasswordForm();
        form.setEmail("user@example.com");
        form.setOtp("123456");
        form.setNewPassword("newPassword123");
        form.setConfirmNewPassword("newPassword123");

        assertEquals("user@example.com", form.getEmail());
        assertEquals("123456", form.getOtp());
        assertEquals("newPassword123", form.getNewPassword());
        assertEquals("newPassword123", form.getConfirmNewPassword());
    }
}