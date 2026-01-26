package com.modulink.service.UserModules.GTM;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.Relazioni.Assegnazione.AssegnazioneService;
import com.modulink.Model.Relazioni.Associazione.AssociazioneService;
import com.modulink.Model.Ruolo.RuoloService;
import com.modulink.Model.Task.TaskService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UserNotFoundException;
import com.modulink.Model.Utente.UtenteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "activate.databasepop=false")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GTMControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private AssegnazioneService assegnazioneService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private RuoloService ruoloService;

    @MockitoBean
    private AssociazioneService associazioneService;

    @MockitoBean
    private ModuloService moduloService;

    private UtenteEntity testUtente;

    @BeforeEach
    void setUp() {
        AziendaEntity azienda = new AziendaEntity();
        azienda.setId_azienda(1);

        testUtente = new UtenteEntity();
        testUtente.setEmail("robbencito@gmail.com");
        testUtente.setAzienda(azienda);
        testUtente.setId_utente(1);
        testUtente.setNome("Roberto");
        testUtente.setCognome("Cito");

        UserDetails userDetails = User.withUsername("robbencito@gmail.com")
                .password("password")
                .roles("USER")
                .build();

        when(customUserDetailsService.loadUserByUsername("robbencito@gmail.com")).thenReturn(userDetails);
        when(customUserDetailsService.findByEmail("robbencito@gmail.com")).thenReturn(Optional.of(testUtente));
        when(moduloService.isAccessibleModulo(eq(5), any(UtenteEntity.class))).thenReturn(true);
        when(moduloService.findModuliByUtente(any(UtenteEntity.class))).thenReturn(new ArrayList<>());
    }

    /**
     * Test Case TC1_GTM1: Verifica che la creazione di una task fallisca se il titolo è vuoto.
     * Input: Titolo="" (Vuoto)
     * Output atteso: Errore di validazione sul campo titolo.
     */
    @Test
    @Order(1)
    public void TC1_GTM1_TitoloVuoto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gtm")
                        .with(user("robbencito@gmail.com"))
                        .param("titolo", "")
                        .param("priorita", "3")
                        .param("scadenza", "2026-02-01"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gtm/GestioneTask"))
                .andExpect(model().attributeHasFieldErrors("form", "titolo"));
    }

    /**
     * Test Case TC2_GTM1: Verifica che la creazione di una task fallisca se il titolo è minore di 2 caratteri.
     * Input: Titolo="R" (<2), Priorità="3" (Media)
     * Output atteso: Errore di validazione sul campo titolo.
     */
    @Test
    @Order(2)
    public void TC2_GTM1_TitoloCorto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gtm")
                        .with(user("robbencito@gmail.com"))
                        .param("titolo", "R")
                        .param("priorita", "3")
                        .param("scadenza", "2026-01-01"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gtm/GestioneTask"))
                .andExpect(model().attributeHasFieldErrors("form", "titolo"));
    }

    /**
     * Test Case TC3_GTM1: Verifica che la creazione di una task fallisca se il titolo supera 150 caratteri.
     * Input: Titolo="Redazione..." (>150), Priorità="2"
     * Output atteso: Errore di validazione sul campo titolo.
     */
    @Test
    @Order(3)
    public void TC3_GTM1_TitoloLungo() throws Exception {
        String titoloLungo = "A".repeat(151);
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gtm")
                        .with(user("robbencito@gmail.com"))
                        .param("titolo", titoloLungo)
                        .param("priorita", "2")
                        .param("scadenza", "2026-03-01"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gtm/GestioneTask"))
                .andExpect(model().attributeHasFieldErrors("form", "titolo"));
    }

    /**
     * Test Case TC4_GTM1: Verifica che la creazione di una task fallisca se la priorità è minore di 0.
     * Input: Titolo="Redazione", Priorità="-1"
     * Output atteso: Errore di validazione sul campo priorita.
     */
    @Test
    @Order(4)
    public void TC4_GTM1_PrioritaInvalida() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gtm")
                        .with(user("robbencito@gmail.com"))
                        .param("titolo", "Redazione")
                        .param("priorita", "-1")
                        .param("scadenza", "2026-03-01"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gtm/GestioneTask"))
                .andExpect(model().attributeHasFieldErrors("form", "priorita"));
    }

    /**
     * Test Case TC5_GTM1: Verifica che la creazione di una task fallisca se la priorità è maggiore di 5.
     * Input: Titolo="Redazione", Priorità="6"
     * Output atteso: Errore di validazione sul campo priorita.
     */
    @Test
    @Order(5)
    public void TC5_GTM1_PrioritaAlta() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gtm")
                        .with(user("robbencito@gmail.com"))
                        .param("titolo", "Redazione")
                        .param("priorita", "6")
                        .param("scadenza", "2026-03-01"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gtm/GestioneTask"))
                .andExpect(model().attributeHasFieldErrors("form", "priorita"));
    }

    /**
     * Test Case TC6_GTM1: Verifica che la creazione di una task fallisca se la scadenza è antecedente alla data di creazione.
     * Input: Titolo="Redazione", Priorità="2", Scadenza="01/01/2026" (Antecedente alla data odierna/creazione)
     * Output atteso: Errore di validazione sul campo scadenza.
     */
    @Test
    @Order(6)
    public void TC6_GTM1_ScadenzaAntecedente() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gtm")
                        .with(user("robbencito@gmail.com"))
                        .param("titolo", "Redazione")
                        .param("priorita", "2")
                        .param("scadenza", "2026-01-01"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("moduli/gtm/GestioneTask"))
                .andExpect(model().attributeHasFieldErrors("form", "scadenza"));
    }

    /**
     * Test Case TC7_GTM1: Verifica che la creazione di una task fallisca se l'utente assegnato non esiste.
     * Input: Titolo="Redazione", Priorità="2", Utente="Mario Rossi" (Inesistente)
     * Output atteso: Errore "Utente non trovato".
     */
    @Test
    @Order(7)
    public void TC7_GTM1_UtenteAssegnatoNonEsiste() throws Exception {
        when(customUserDetailsService.getByID(eq(99), any(Integer.class))).thenThrow(new UserNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gtm")
                        .with(user("robbencito@gmail.com"))
                        .param("titolo", "Redazione")
                        .param("priorita", "2")
                        .param("scadenza", "2026-03-01")
                        .param("messaggi[0].id", "99")
                        .param("messaggi[0].type", "utente")
                        .param("messaggi[0].azienda", "1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/dashboard/gtm*error=true*message=Utente+non+trovato*"));
    }

    /**
     * Test Case TC8_GTM1: Verifica la creazione con successo di una task con scadenza.
     * Input: Titolo="Redazione", Priorità="3", Scadenza="01/03/2026"
     * Output atteso: Task creata con successo.
     */
    @Test
    @Order(8)
    public void TC8_GTM1_CreazioneSuccessoConScadenza() throws Exception {
        UtenteEntity mario = new UtenteEntity();
        mario.setId_utente(2);
        when(customUserDetailsService.getByID(eq(2), any(Integer.class))).thenReturn(mario);

        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gtm")
                        .with(user("robbencito@gmail.com"))
                        .param("titolo", "Redazione")
                        .param("priorita", "3")
                        .param("scadenza", "2026-03-01")
                        .param("messaggi[0].id", "2")
                        .param("messaggi[0].type", "utente")
                        .param("messaggi[0].azienda", "1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/dashboard/gtm*success=true*message=Task+creata+con+successo*"));
    }

    /**
     * Test Case TC9_GTM1: Verifica la creazione con successo di una task senza scadenza.
     * Input: Titolo="Redazione", Priorità="3", Scadenza="" (Vuota)
     * Output atteso: Task creata con successo.
     */
    @Test
    @Order(9)
    public void TC9_GTM1_CreazioneSuccessoSenzaScadenza() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/gtm")
                        .with(user("robbencito@gmail.com"))
                        .param("titolo", "Redazione")
                        .param("priorita", "3")
                        .param("scadenza", ""))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/dashboard/gtm*success=true*message=Task+creata+con+successo*"));
    }
}
