package com.modulink.model;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuoloEntityCase {
    @Test
    public void testGetterSetterRuoloEntity() {
        AziendaEntity modulink = new AziendaEntity("ModuLink","1111111111","Via Nazionale","Santa Maria a Vico","81028","+393471304385","azienda-logos/11111111111_1769189367649_logo.png");
        RuoloEntity ruolo=new RuoloEntity(modulink,"Responsabile","FF0000","Ruolo Responsabile");
        assertThat(ruolo.getAzienda()).isEqualTo(modulink);
        assertEquals("Responsabile",ruolo.getNome());
        assertEquals("FF0000",ruolo.getColore());
        assertEquals("Ruolo Responsabile",ruolo.getDescrizione());

        AziendaEntity B = new AziendaEntity("Iphone", "12345678913", "Via Alfredo Capone 12", "Roma", "00120", "3471304385", "/Azienda-logos/foto.jpeg");
        ruolo.setAzienda(B);
        assertThat(ruolo.getAzienda()).isEqualTo(B);
        ruolo.setNome("Responsabile 2");
        assertEquals("Responsabile 2",ruolo.getNome());
        ruolo.setDescrizione("Nuovo ruolo");
        assertEquals("Nuovo ruolo",ruolo.getDescrizione());
        ruolo.setColore("#000000");
        assertEquals("#000000",ruolo.getColore());
    }
}