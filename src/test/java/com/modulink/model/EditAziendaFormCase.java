package com.modulink.model;

import com.modulink.Controller.UserModules.GDR.EditAziendaForm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EditAziendaFormCase {

    @Test
    public void TestGetterAndSetter() {
        EditAziendaForm form = new EditAziendaForm();
        form.setNomeAzienda("ModuLink Srl");
        form.setPiva("12345678901");
        form.setIndirizzo("Via Roma 1");
        form.setCitta("Milano");
        form.setCap("20100");
        form.setTelefono("+39 02 1234567");
        form.setDeleteFoto(true);
        
        byte[] bytes = "new logo content".getBytes();
        form.setLogoBytes(bytes);
        form.setLogoFileName("new_logo.png");

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
        assertTrue(form.isDeleteFoto());
        assertArrayEquals(bytes, form.getLogoBytes());
        assertEquals("new_logo.png", form.getLogoFileName());
        assertEquals(file, form.getLogo());
    }
}
