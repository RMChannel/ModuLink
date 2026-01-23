package com.modulink.model;

import com.modulink.Controller.AdminModules.Support.SupportForm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SupportFormCase {

    @Test
    public void TestGetterAndSetter() {
        SupportForm supportForm = new SupportForm();
        supportForm.setEmail("test@example.com");
        supportForm.setCategory("Technical");
        supportForm.setMessage("This is a test message.");

        assertEquals("test@example.com", supportForm.getEmail());
        assertEquals("Technical", supportForm.getCategory());
        assertEquals("This is a test message.", supportForm.getMessage());
    }

    @Test
    public void TestConstructor() {
        SupportForm supportForm = new SupportForm("admin@modulink.com", "Commercial", "Inquiry about pricing");

        assertEquals("admin@modulink.com", supportForm.getEmail());
        assertEquals("Commercial", supportForm.getCategory());
        assertEquals("Inquiry about pricing", supportForm.getMessage());
    }
}