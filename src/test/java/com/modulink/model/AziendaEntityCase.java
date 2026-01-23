package com.modulink.model;

import com.modulink.Model.Azienda.AziendaEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AziendaEntityCase {
    @Test
    public void testGetterSetterAziendaEntity() {
        AziendaEntity A = new AziendaEntity("Modulink", "12345678912", "Via Alfredo Capone 11", "Salerno", "84128", "3285630871", "");
        assertEquals("Modulink", A.getNome());
        assertEquals("12345678912", A.getPiva());
        assertEquals("Via Alfredo Capone 11", A.getIndirizzo());
        assertEquals("Salerno", A.getCitta());
        assertEquals("84128", A.getCap());
        assertEquals("3285630871", A.getTelefono());
        assertEquals("", A.getLogo());

        //Test setter
        A.setNome("Iphone");
        assertEquals("Iphone", A.getNome());

        A.setPiva("12345678913");
        assertEquals("12345678913", A.getPiva());

        A.setIndirizzo("Via Alfredo Capone 12");
        assertEquals("Via Alfredo Capone 12", A.getIndirizzo());

        A.setCitta("Roma");
        assertEquals("Roma", A.getCitta());

        A.setCap("00120");
        assertEquals("00120", A.getCap());

        A.setTelefono("3471304385");
        assertEquals("3471304385", A.getTelefono());

        A.setLogo("/Azienda-logos/foto.jpeg");
        assertEquals("/Azienda-logos/foto.jpeg", A.getLogo());
    }
}
