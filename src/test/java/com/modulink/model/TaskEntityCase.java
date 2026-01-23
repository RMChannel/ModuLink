package com.modulink.model;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Task.TaskEntity;
import com.modulink.Model.Utente.PasswordUtility;
import com.modulink.Model.Utente.UtenteEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

public class TaskEntityCase {
    @Test
    public void testGetterSetterTaskEntity() {
        AziendaEntity modulink = new AziendaEntity("ModuLink","1111111111","Via Nazionale","Santa Maria a Vico","81028","+393471304385","azienda-logos/11111111111_1769189367649_logo.png");
        UtenteEntity u=new UtenteEntity(modulink,"r.cito@studenti.unisa.it", PasswordUtility.hashPassword("ciaociao"),"Roberto","Cito","+393471304385","");
        TaskEntity task=new TaskEntity(modulink,u,"Test Task",5, LocalDate.of(2026,12,1),LocalDate.now(),null);

        assertThat(task.getAzienda()).isEqualTo(modulink);
        assertThat(task.getUtenteCreatore()).isEqualTo(u);
        assertEquals("Test Task",task.getTitolo());
        assertEquals(5,task.getPriorita());
        assertThat(task.getScadenza()).isEqualTo(LocalDate.of(2026,12,1));
        assertThat(task.getDataCreazione()).isEqualTo(LocalDate.now());
        assertThat(task.getDataCompletamento()).isNull();

        AziendaEntity B = new AziendaEntity("Iphone", "12345678913", "Via Alfredo Capone 12", "Roma", "00120", "3471304385", "/Azienda-logos/foto.jpeg");
        task.setAzienda(B);
        assertThat(task.getAzienda()).isEqualTo(B);
        UtenteEntity u2=new UtenteEntity(B,"a.buzi@studenti.unisa.it",PasswordUtility.hashPassword("ciaociao2"),"Arjel","Buzi","+393755676630","");
        task.setUtenteCreatore(u2);
        assertThat(task.getUtenteCreatore()).isEqualTo(u2);
        task.setTitolo("Test Task 2");
        assertEquals("Test Task 2",task.getTitolo());
        task.setPriorita(10);
        assertEquals(10,task.getPriorita());
        task.setScadenza(LocalDate.of(2026,12,2));
        assertThat(task.getScadenza()).isEqualTo(LocalDate.of(2026,12,2));
        task.setCompletato(LocalDate.of(2026,12,3));
        assertThat(task.getDataCompletamento()).isEqualTo(LocalDate.of(2026,12,3));
    }
}
