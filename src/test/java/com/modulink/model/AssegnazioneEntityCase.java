package com.modulink.model;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Assegnazione.AssegnazioneEntity;
import com.modulink.Model.Task.TaskEntity;
import com.modulink.Model.Utente.PasswordUtility;
import com.modulink.Model.Utente.UtenteEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssegnazioneEntityCase {
    @Test
    public void AssegnzioneEntity(){
        AziendaEntity A = new AziendaEntity("Modulink", "12345678912", "Via Alfredo Capone 11", "Salerno", "84128", "3285630871", "");
        UtenteEntity UE = new UtenteEntity(A, "leqsochikviladze77gmail.com", PasswordUtility.hashPassword("ciaociao"),"leqso", "chikviladze", "3285630871", "/Utente-foto/foto.jpeg");
        TaskEntity TE= new TaskEntity(A, UE, "Creazione", 3, LocalDate.of(2026, 1, 31), LocalDate.of(2026, 1, 21), LocalDate.of(2026,1, 30));
        AssegnazioneEntity AE= new AssegnazioneEntity(TE, UE);
        assertThat(AE.getTask()).isEqualTo(TE);
        assertThat(AE.getUtente()).isEqualTo(UE);
        assertEquals(AE.getId_task(), TE.getId_task());
        assertEquals(AE.getId_azienda(), TE.getAzienda().getId_azienda());
        assertEquals(AE.getId_utente(), UE.getId_utente());

        AziendaEntity B = new AziendaEntity("Iphone", "12345678913", "Via Alfredo Capone 12", "Roma", "00120", "3471304385", "/Azienda-logos/foto.jpeg");
        UtenteEntity UE1= new UtenteEntity(B, "leqsochikviladze@gmail.com", PasswordUtility.hashPassword("Ciao"), "leqso1", "chiko", "3895010125", "/Utente-foto/foto1.jpeg");
        TaskEntity TE1 = new TaskEntity(B, UE1, "Distruzione", 2, LocalDate.of(2026, 2, 5),  LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2 , 4));
        AE.setTask(TE1);
        assertThat(AE.getTask()).as("nuovo task").isEqualTo(TE1);
        assertThat(AE.getUtente()).as("nuovo utente").isEqualTo(UE1);
        assertEquals(TE1.getId_task(), AE.getTask().getId_task());
        assertEquals(UE1.getId_utente(), AE.getUtente().getId_utente());



    }
}
