package com.modulink.model;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Eventi.EventoEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventoEntityCase {
    @Test
    public void testGetterSetterEventoEntity(){
        AziendaEntity A = new AziendaEntity("Modulink", "12345678912", "Via Alfredo Capone 11", "Salerno", "84128", "3285630871", "");
        EventoEntity E = new EventoEntity( A , "Riunione", "Sala Riunioni", LocalDateTime.of(2026, 1, 28, 12, 0), LocalDateTime.of(2026, 1, 28, 13, 0));
        assertThat(E.getAzienda()).isEqualTo(A);
        assertEquals("Riunione", E.getNome());
        assertEquals("Sala Riunioni", E.getLuogo());
        assertEquals(LocalDateTime.of(2026, 1, 28, 12, 0), E.getData_ora_inizio());
        assertEquals(LocalDateTime.of(2026, 1, 28, 13, 0), E.getData_fine());

        AziendaEntity B = new AziendaEntity("Iphone", "12345678913", "Via Alfredo Capone 12", "Roma", "00120", "3471304385", "/Azienda-logos/foto.jpeg");
        E.setAzienda(B);
        assertThat(E.getAzienda()).isEqualTo(B);

        E.setNome("Pranzo");
        assertEquals("Pranzo", E.getNome());

        E.setLuogo("Sala pranzo");
        assertEquals("Sala pranzo",E.getLuogo());

        E.setData_ora_inizio(LocalDateTime.of(2026, 1, 28, 13, 0));
        assertEquals(LocalDateTime.of(2026, 1, 28, 13, 0), E.getData_ora_inizio());

        E.setData_fine(LocalDateTime.of(2026, 1, 28, 14, 0));
        assertEquals(LocalDateTime.of(2026, 1, 28, 14, 0), E.getData_fine());

    }

}
