package com.modulink.model;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Relazioni.Assegnazione.AssegnazioneEntity;
import com.modulink.Model.Relazioni.Associazione.AssociazioneEntity;
import com.modulink.Model.Ruolo.RuoloEntity;
import com.modulink.Model.Task.TaskEntity;
import com.modulink.Model.Utente.PasswordUtility;
import com.modulink.Model.Utente.UtenteEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssociazioneEntityCase {
    @Test
    public void AssociazioneEntity(){
        AziendaEntity A = new AziendaEntity("Modulink", "12345678912", "Via Alfredo Capone 11", "Salerno", "84128", "3285630871", "");
        UtenteEntity UE = new UtenteEntity(A, "leqsochikviladze77gmail.com", PasswordUtility.hashPassword("ciaociao"),"leqso", "chikviladze", "3285630871", "/Utente-foto/foto.jpeg");
        RuoloEntity RE = new RuoloEntity(A, "Responsabile", "Rosso", "Gestisce tutto");
        AssociazioneEntity AE = new AssociazioneEntity(UE, RE);
        assertThat(AE.getUtente()).isEqualTo(UE);
        assertThat(AE.getRuolo()).isEqualTo(RE);
        assertEquals(AE.getId_utente(), UE.getId_utente());
        assertEquals(AE.getId_ruolo(), RE.getId_ruolo());
    }
}
