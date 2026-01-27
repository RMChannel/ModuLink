package com.modulink.model;

import com.modulink.Controller.UserModules.GTM.GTMForm;
import com.modulink.Controller.UserModules.GTM.GTMMessage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GTMFormCase {

    @Test
    public void TestGetterAndSetter() {
        GTMForm form = new GTMForm();
        form.setTitolo("Complete project");
        form.setPriorita(1);
        LocalDate date = LocalDate.now().plusDays(7);
        form.setScadenza(date);
        
        List<GTMMessage> messages = new ArrayList<>();
        messages.add(new GTMMessage("User 1", "utente", 1, 1, "#FFFFFF"));
        form.setMessaggi(messages);

        assertEquals("Complete project", form.getTitolo());
        assertEquals(1, form.getPriorita());
        assertEquals(date, form.getScadenza());
        assertEquals(messages, form.getMessaggi());
    }
}
