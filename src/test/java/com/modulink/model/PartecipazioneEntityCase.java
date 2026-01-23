package com.modulink.model;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Eventi.EventoEntity;
import com.modulink.Model.Relazioni.Partecipazione.PartecipazioneEntity;
import com.modulink.Model.Utente.PasswordUtility;
import com.modulink.Model.Utente.UtenteEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartecipazioneEntityCase {
    @Test
    public void PartecipazioneEntity(){
        AziendaEntity A = new AziendaEntity("Modulink", "12345678912", "Via Alfredo Capone 11", "Salerno", "84128", "3285630871", "");
        UtenteEntity UE = new UtenteEntity(A, "leqsochikviladze77gmail.com", PasswordUtility.hashPassword("ciaociao"),"leqso", "chikviladze", "3285630871", "/Utente-foto/foto.jpeg");
        EventoEntity E = new EventoEntity( A , "Riunione", "Sala Riunioni", LocalDateTime.of(2026, 1, 28, 12, 0), LocalDateTime.of(2026, 1, 28, 13, 0));
        PartecipazioneEntity Pe= new PartecipazioneEntity(UE.getId_utente(), E.getId_evento(), A.getId_azienda());
        assertEquals(Pe.getId_utente(), UE.getId_utente());
        assertEquals(Pe.getId_azienda(), A.getId_azienda());
        assertEquals(Pe.getId_evento(), UE.getId_utente());
        AziendaEntity B = new AziendaEntity("Iphone", "12345678913", "Via Alfredo Capone 12", "Roma", "00120", "3471304385", "/Azienda-logos/foto.jpeg");
        UtenteEntity UE1= new UtenteEntity(B, "leqsochikviladze@gmail.com", PasswordUtility.hashPassword("Ciao"), "leqso1", "chiko", "3895010125", "/Utente-foto/foto1.jpeg");
        EventoEntity E1 = new EventoEntity(B , "Pranzo", "Sala pranzo", LocalDateTime.of(2026, 1, 28, 13, 0), LocalDateTime.of(2026, 1, 28, 14, 0));
        Pe.setEvento(E1);
        assertThat(Pe.getEvento()).isEqualTo(E1);
        Pe.setUtente(UE1);
        assertEquals(Pe.getId_utente(), UE1.getId_utente());
        Pe.setId_azienda(123);
        assertThat(Pe.getId_azienda()).isEqualTo(123);

    }
}
