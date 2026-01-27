package com.modulink.model;

import com.modulink.Model.News.NewsEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NewsEntityCase {
    @Test
    public void testGetterSetterNewsEntity(){
        NewsEntity NE = new NewsEntity("Pubblicazione", "contenuto del testo", LocalDate.of(2026, 1, 31));
        assertEquals("Pubblicazione", NE.getTitolo());
        assertEquals("contenuto del testo", NE.getTesto());
        assertEquals(LocalDate.of(2026, 1, 31), NE.getData());

        NE.setTitolo("Denuncia");
        assertEquals("Denuncia", NE.getTitolo());

        NE.setTesto("Contenuto della denuncia");
        assertEquals("Contenuto della denuncia", NE.getTesto());

        NE.setData(LocalDate.of(2026, 1, 31));
        assertEquals(LocalDate.of(2026, 1, 31), NE.getData());
    }
}
