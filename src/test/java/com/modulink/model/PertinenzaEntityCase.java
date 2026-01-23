package com.modulink.model;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import com.modulink.Model.Relazioni.Pertinenza.PertinenzaEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PertinenzaEntityCase {
    @Test
    public void PertinenzaEntity(){
        AziendaEntity A = new AziendaEntity("Modulink", "12345678912", "Via Alfredo Capone 11", "Salerno", "84128", "3285630871", "");
        ModuloEntity M = new ModuloEntity(1, "Store", "Permette di comprare moduli", "/dashboard/GDM", "/dashboard-foto/foto.jpeg", true);
        RuoloEntity ruolo=new RuoloEntity(A,"Responsabile","FF0000","Ruolo Responsabile");
        AttivazioneEntity AE = new AttivazioneEntity(M, A);
        PertinenzaEntity Pe= new PertinenzaEntity(ruolo.getId_ruolo(), M.getId_modulo(), A.getId_azienda());
        assertEquals(Pe.getId_azienda(), A.getId_azienda());
        assertEquals(Pe.getId_ruolo(), ruolo.getId_ruolo());
        assertEquals(Pe.getId_modulo(), M.getId_modulo());

        AziendaEntity B = new AziendaEntity("Iphone", "12345678913", "Via Alfredo Capone 12", "Roma", "00120", "3471304385", "/Azienda-logos/foto.jpeg");
        ModuloEntity M1 = new ModuloEntity(2, "Gestione Utenti", "Permette di gestire utenti", "/dashboard/GDU", "/dashboard-foto/fotoq.jpeg", false);
        RuoloEntity ruolo1 = new RuoloEntity(A,"Utente","FF0000","Ruolo utente");
        AttivazioneEntity AE1= new AttivazioneEntity(M1, B);
        Pe.setId_azienda(B.getId_azienda());
        assertEquals(Pe.getId_azienda(), B.getId_azienda());

        Pe.setRuolo(ruolo1);
        assertThat(ruolo1).isEqualTo(Pe.getRuolo());

        Pe.setAttivazione(AE1);
        assertThat(AE1).isEqualTo(Pe.getAttivazione());



    }
}
