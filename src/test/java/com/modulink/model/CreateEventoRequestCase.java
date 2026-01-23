package com.modulink.model;

import com.modulink.Controller.UserModules.GDE.EventoController.CreateEventoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CreateEventoRequestCase {

    @Test
    public void TestRecord() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 27, 14, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 27, 16, 0);
        List<Integer> participants = List.of(4, 5);
        CreateEventoRequest request = new CreateEventoRequest("New Event", "Hall", start, end, participants);

        assertEquals("New Event", request.nome());
        assertEquals("Hall", request.luogo());
        assertEquals(start, request.inizio());
        assertEquals(end, request.fine());
        assertEquals(participants, request.partecipanti());
    }
}
