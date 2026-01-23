package com.modulink.model;

import com.modulink.Controller.UserModules.GDU.NewUserForm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NewUserFormCase {

    @Test
    public void TestGetterAndSetter() {
        NewUserForm form = new NewUserForm();
        form.setNome("Anna");
        form.setCognome("Bianchi");
        form.setEmail("anna.bianchi@example.com");
        form.setTelefono("+39 333 9876543");

        assertEquals("Anna", form.getNome());
        assertEquals("Bianchi", form.getCognome());
        assertEquals("anna.bianchi@example.com", form.getEmail());
        assertEquals("+39 333 9876543", form.getTelefono());
    }

    @Test
    public void TestConstructor() {
        NewUserForm form = new NewUserForm("Pietro", "Neri", "pietro.neri@example.com", "+39 333 1112233");

        assertEquals("Pietro", form.getNome());
        assertEquals("Neri", form.getCognome());
        assertEquals("pietro.neri@example.com", form.getEmail());
        assertEquals("+39 333 1112233", form.getTelefono());
    }
}
