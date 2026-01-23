package com.modulink.model;

import com.modulink.Controller.UserModules.GDE.EventoController.EventoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EventoDTOCase {

    @Test
    public void TestRecord() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 27, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 27, 12, 0);
        EventoDTO dto = new EventoDTO(1, "Meeting", "Office", start, end);

        assertEquals(1, dto.id_evento());
        assertEquals("Meeting", dto.nome());
        assertEquals("Office", dto.luogo());
        assertEquals(start, dto.data_ora_inizio());
        assertEquals(end, dto.data_fine());
    }
}
