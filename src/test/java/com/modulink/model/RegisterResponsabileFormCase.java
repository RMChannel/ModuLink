package com.modulink.model;

import com.modulink.Controller.Register.RegisterResponsabileForm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RegisterResponsabileFormCase {

    @Test
    public void TestGetterAndSetter() {
        RegisterResponsabileForm form = new RegisterResponsabileForm();
        form.setEmail("admin@example.com");
        form.setPassword("securePass123");
        form.setConfermaPassword("securePass123");
        form.setNome("Luigi");
        form.setCognome("Verdi");
        form.setTelefonoutente("+39 347 7654321");

        MockMultipartFile file = new MockMultipartFile("immagineProfilo", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        form.setImmagineProfilo(file);

        assertEquals("admin@example.com", form.getEmail());
        assertEquals("securePass123", form.getPassword());
        assertEquals("securePass123", form.getConfermaPassword());
        assertEquals("Luigi", form.getNome());
        assertEquals("Verdi", form.getCognome());
        assertEquals("+39 347 7654321", form.getTelefonoutente());
        assertEquals(file, form.getImmagineProfilo());
    }
}
