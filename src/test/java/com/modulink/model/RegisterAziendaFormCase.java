package com.modulink.model;

import com.modulink.Controller.Register.RegisterAziendaForm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class RegisterAziendaFormCase {

    @Test
    public void TestGetterAndSetter() {
        RegisterAziendaForm form = new RegisterAziendaForm();
        form.setNomeAzienda("ModuLink Srl");
        form.setPiva("12345678901");
        form.setIndirizzo("Via Roma 1");
        form.setCitta("Milano");
        form.setCap("20100");
        form.setTelefono("+39 02 1234567");
        
        byte[] bytes = "logo content".getBytes();
        form.setLogoBytes(bytes);
        form.setLogoFileName("logo.png");

        byte[] content = new byte[1024 * 1024 * 13];
        MockMultipartFile file = new MockMultipartFile(
                "immagineProfilo",
                "Foto.jpg",
                "image/jpeg",
                content
        );
        form.setLogo(file);

        assertEquals("ModuLink Srl", form.getNomeAzienda());
        assertEquals("12345678901", form.getPiva());
        assertEquals("Via Roma 1", form.getIndirizzo());
        assertEquals("Milano", form.getCitta());
        assertEquals("20100", form.getCap());
        assertEquals("+39 02 1234567", form.getTelefono());
        assertArrayEquals(bytes, form.getLogoBytes());
        assertEquals("logo.png", form.getLogoFileName());
        assertEquals(file, form.getLogo());
    }
}