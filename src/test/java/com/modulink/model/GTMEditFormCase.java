package com.modulink.model;

import com.modulink.Controller.UserModules.GTM.GTMEditForm;
import com.modulink.Controller.UserModules.GTM.GTMMessage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GTMEditFormCase {

    @Test
    public void TestGetterAndSetter() {
        GTMEditForm form = new GTMEditForm();
        form.setIdTask(100);
        form.setTitolo("Update docs");
        form.setPriorita(2);
        LocalDate date = LocalDate.now().plusDays(3);
        form.setScadenza(date);
        form.setCompletato(true);
        
        List<GTMMessage> messages = new ArrayList<>();
        messages.add(new GTMMessage("Role 1", "ruolo", 2, 1, "#000000"));
        form.setMessaggi(messages);

        assertEquals(100, form.getIdTask());
        assertEquals("Update docs", form.getTitolo());
        assertEquals(2, form.getPriorita());
        assertEquals(date, form.getScadenza());
        assertTrue(form.isCompletato());
        assertEquals(messages, form.getMessaggi());
    }
}
