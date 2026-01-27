package com.modulink.model;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloEntity;
import com.modulink.Model.Relazioni.Attivazione.AttivazioneEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AttivazioneEntityCase {
    @Test
    public void AttivazioneEntity(){
        AziendaEntity A = new AziendaEntity("Modulink", "12345678912", "Via Alfredo Capone 11", "Salerno", "84128", "3285630871", "");
        ModuloEntity M = new ModuloEntity(1, "Store", "Permette di comprare moduli", "/dashboard/GDM", "/dashboard-foto/foto.jpeg", true);
        AttivazioneEntity AE = new AttivazioneEntity(M, A);
        assertThat(AE.getModulo()).isEqualTo(M);
        assertThat(AE.getAzienda()).isEqualTo(A);

        AziendaEntity B = new AziendaEntity("Iphone", "12345678913", "Via Alfredo Capone 12", "Roma", "00120", "3471304385", "/Azienda-logos/foto.jpeg");
        ModuloEntity M1 = new ModuloEntity(1, "Gestione utente", "Gestisce utenti", "/dashboard/GDU", "/dashboard-foto/foto1.jpeg", false);

        AE.setModulo(M1);
        assertThat(AE.getModulo()).isEqualTo(M1);

        AE.setAzienda(B);
        assertThat(AE.getAzienda()).isEqualTo(B);


    }
}
