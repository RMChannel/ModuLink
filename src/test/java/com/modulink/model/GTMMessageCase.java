package com.modulink.model;

import com.modulink.Controller.UserModules.GTM.GTMMessage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GTMMessageCase {

    @Test
    public void TestGetterAndSetter() {
        GTMMessage msg = new GTMMessage();
        msg.setNome("Test Name");
        msg.setType("utente");
        msg.setId(5);
        msg.setAzienda(1);
        msg.setColor("#FF5500");

        assertEquals("Test Name", msg.getNome());
        assertEquals("utente", msg.getType());
        assertEquals(5, msg.getId());
        assertEquals(1, msg.getAzienda());
        assertEquals("#FF5500", msg.getColor());
    }

    @Test
    public void TestConstructor() {
        GTMMessage msg = new GTMMessage("Admin", "ruolo", 1, 1, "#000000");

        assertEquals("Admin", msg.getNome());
        assertEquals("ruolo", msg.getType());
        assertEquals(1, msg.getId());
        assertEquals(1, msg.getAzienda());
        assertEquals("#000000", msg.getColor());
    }

    @Test
    public void TestToString() {
        GTMMessage msg = new GTMMessage("Name", "type", 1, 2, "color");
        String expected = "GTMMessage{nome='Name', type='type', id=1, azienda=2, color='color'}";
        assertEquals(expected, msg.toString());
    }
}
