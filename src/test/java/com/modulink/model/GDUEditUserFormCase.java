package com.modulink.model;

import com.modulink.Controller.UserModules.GDU.EditUserForm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GDUEditUserFormCase {

    @Test
    public void TestGetterAndSetter() {
        EditUserForm form = new EditUserForm("Nome", "Cognome", "new@mail.com", "old@mail.com", "123456", "newPass", "newPass");
        
        form.setNome("Mario");
        form.setCognome("Rossi");
        form.setEmail("mario@mail.com");
        form.setOldmail("old@mail.com");
        form.setTelefono("987654");
        form.setNewPassword("pass123");
        form.setConfirmNewPassword("pass123");

        assertEquals("Mario", form.getNome());
        assertEquals("Rossi", form.getCognome());
        assertEquals("mario@mail.com", form.getEmail());
        assertEquals("old@mail.com", form.getOldmail());
        assertEquals("987654", form.getTelefono());
        assertEquals("pass123", form.getNewPassword());
        assertEquals("pass123", form.getConfirmNewPassword());
    }

    @Test
    public void TestConstructor() {
        EditUserForm form = new EditUserForm("Nome", "Cognome", "new@mail.com", "old@mail.com", "123456", "newPass", "newPass");

        assertEquals("Nome", form.getNome());
        assertEquals("Cognome", form.getCognome());
        assertEquals("new@mail.com", form.getEmail());
        assertEquals("old@mail.com", form.getOldmail());
        assertEquals("123456", form.getTelefono());
        assertEquals("newPass", form.getNewPassword());
        assertEquals("newPass", form.getConfirmNewPassword());
    }
}
