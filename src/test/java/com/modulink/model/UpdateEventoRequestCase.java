package com.modulink.model;

import com.modulink.Controller.UserModules.GDE.EventoController.UpdateEventoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UpdateEventoRequestCase {

    @Test
    public void TestRecord() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 27, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 27, 12, 0);
        List<Integer> participants = List.of(1, 2, 3);
        UpdateEventoRequest request = new UpdateEventoRequest(1, "Updated Meeting", "Remote", start, end, participants);

        assertEquals(1, request.id());
        assertEquals("Updated Meeting", request.nome());
        assertEquals("Remote", request.luogo());
        assertEquals(start, request.inizio());
        assertEquals(end, request.fine());
        assertEquals(participants, request.partecipanti());
    }
}
