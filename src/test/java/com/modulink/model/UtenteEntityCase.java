package com.modulink.model;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Utente.PasswordUtility;
import com.modulink.Model.Utente.UtenteEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtenteEntityCase {
    @Test
    public void testGetterSetterUtenteEntity() {
        AziendaEntity modulink = new AziendaEntity("ModuLink","1111111111","Via Nazionale","Santa Maria a Vico","81028","+393471304385","azienda-logos/11111111111_1769189367649_logo.png");
        UtenteEntity u=new UtenteEntity(modulink,"r.cito@studenti.unisa.it", PasswordUtility.hashPassword("ciaociao"),"Roberto","Cito","+393471304385","");
        assertThat(u.getAzienda()).isEqualTo(modulink);
        assertEquals("r.cito@studenti.unisa.it", u.getEmail());
        assertEquals("Roberto", u.getNome());
        assertEquals("Cito", u.getCognome());
        assertThat(PasswordUtility.checkPassword("ciaociao", u.getHash_password())).isTrue();
        assertEquals("+393471304385", u.getTelefono());
        assertEquals("",u.getPath_immagine_profilo());

        AziendaEntity B = new AziendaEntity("Iphone", "12345678913", "Via Alfredo Capone 12", "Roma", "00120", "3471304385", "/Azienda-logos/foto.jpeg");
        u.setAzienda(B);
        assertThat(u.getAzienda()).isEqualTo(B);
        u.setEmail("a.buzi@studenti.unisa.it");
        assertEquals("a.buzi@studenti.unisa.it", u.getEmail());
        u.setNome("Arjel");
        assertEquals("Arjel", u.getNome());
        u.setCognome("Buzi");
        assertEquals("Buzi", u.getCognome());
        u.setHash_password(PasswordUtility.hashPassword("ciaociao2"));
        assertThat(PasswordUtility.checkPassword("ciaociao2", u.getHash_password())).isTrue();
        u.setTelefono("+393755676630");
        assertEquals("+393755676630", u.getTelefono());
        u.setPath_immagine_profilo("/user-logos/foto2.jpeg");
        assertEquals("/user-logos/foto2.jpeg", u.getPath_immagine_profilo());
    }
}
