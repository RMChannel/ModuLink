package com.modulink.model;

import com.modulink.Controller.EditUser.EditUserForm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EditUserFormCase {

    @Test
    public void TestGetterAndSetter() {
        EditUserForm form = new EditUserForm();
        form.setNome("Mario");
        form.setCognome("Rossi");
        form.setEmail("mario.rossi@example.com");
        form.setTelefono("+39 333 1234567");
        form.setPassword("password123");
        form.setConfirmPassword("password123");
        form.setRemoveImageFlag(true);
        
        MockMultipartFile file = new MockMultipartFile("immagineProfilo", "test.jpg", "image/jpeg", "test image content".getBytes());
        form.setImmagineProfilo(file);

        assertEquals("Mario", form.getNome());
        assertEquals("Rossi", form.getCognome());
        assertEquals("mario.rossi@example.com", form.getEmail());
        assertEquals("+39 333 1234567", form.getTelefono());
        assertEquals("password123", form.getPassword());
        assertEquals("password123", form.getConfirmPassword());
        assertTrue(form.isRemoveImageFlag());
        assertEquals(file, form.getImmagineProfilo());
    }
}